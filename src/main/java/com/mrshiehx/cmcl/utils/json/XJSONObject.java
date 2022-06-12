package com.mrshiehx.cmcl.utils.json;

import org.json.*;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 按顺序的 JSONObject 工具，转字符串格式化时如果只有单个的项也会缩进（原版不会）
 **/
public class XJSONObject extends JSONObject {
    static final Pattern NUMBER_PATTERN = Pattern.compile("-?(?:0|[1-9]\\d*)(?:\\.\\d+)?(?:[eE][+-]?\\d+)?");

    public XJSONObject() {
        try {
            Field f = JSONObject.class.getDeclaredField("map");
            f.setAccessible(true);
            f.set(this, new LinkedHashMap<>());
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }


    public XJSONObject(String source) throws JSONException {
        this(new XJSONTokener(source));
    }

    public XJSONObject(Map<?, ?> m) {
        try {
            Field f = JSONObject.class.getDeclaredField("map");
            f.setAccessible(true);
            if (m == null) {
                f.set(this, new LinkedHashMap<>());
            } else {
                f.set(this, new LinkedHashMap<>(m.size()));

                for (Map.Entry<?, ?> entry : m.entrySet()) {
                    if (entry.getKey() == null) {
                        throw new NullPointerException("Null key.");
                    }

                    Object value = entry.getValue();
                    if (value != null) {
                        ((LinkedHashMap<String, Object>) f.get(this)).put(String.valueOf(((Map.Entry<?, ?>) entry).getKey()), wrap(value));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public XJSONObject(XJSONTokener x) throws JSONException {
        this();
        if (x.nextClean() != '{') {
            throw x.syntaxError("A JSONObject text must begin with '{'");
        } else {
            while (true) {
                char prev;
                Method method;
                try {
                    method = JSONTokener.class.getDeclaredMethod("getPrevious");
                    method.setAccessible(true);
                    prev = (char) method.invoke(x);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                char c = x.nextClean();
                switch (c) {
                    case '\u0000':
                        throw x.syntaxError("A JSONObject text must end with '}'");
                    case '[':
                    case '{':
                        if (prev == '{') {
                            throw x.syntaxError("A JSON Object can not directly nest another JSON Object or JSON Array.");
                        }
                    default:
                        x.back();
                        String key = x.nextValue().toString();
                        c = x.nextClean();
                        if (c != ':') {
                            throw x.syntaxError("Expected a ':' after a key");
                        }

                        if (key != null) {
                            if (this.opt(key) != null) {
                                throw x.syntaxError("Duplicate key \"" + key + "\"");
                            }

                            Object value = x.nextValue();
                            if (value != null) {
                                this.put(key, value);
                            }
                        }

                        switch (x.nextClean()) {
                            case ',':
                            case ';':
                                if (x.nextClean() == '}') {
                                    return;
                                }

                                x.back();
                                continue;
                            case '}':
                                return;
                            default:
                                throw x.syntaxError("Expected a ',' or '}'");
                        }
                    case '}':
                        return;
                }
            }
        }
    }

    public Writer write(Writer writer, int indentFactor, int indent) throws JSONException {
        try {
            boolean needsComma = false;
            int length = this.length();
            writer.write('{');
            int newIndent = indent + indentFactor;
            if (length == 1) {
                Map.Entry<String, ?> entry = this.entrySet().iterator().next();


                if (indentFactor > 0) {
                    writer.write('\n');
                }

                //indent(writer, newIndent);
                for (int i = 0; i < newIndent; ++i) {
                    writer.write(' ');
                }

                String key = entry.getKey();
                writer.write(quote(key));
                writer.write(':');
                if (indentFactor > 0) {
                    writer.write(' ');
                }

                try {
                    writeValue(writer, entry.getValue(), indentFactor, newIndent);
                } catch (Exception var12) {
                    throw new JSONException("Unable to write JSONObject value for key: " + key, var12);
                }


                if (indentFactor > 0) {
                    writer.write('\n');
                }

                //indent(writer, indent);
                for (int i = 0; i < indent; ++i) {
                    writer.write(' ');
                }

            } else if (length != 0) {

                for (Iterator<Map.Entry<String, Object>> var15 = this.entrySet().iterator(); var15.hasNext(); needsComma = true) {
                    Map.Entry<String, ?> entry = var15.next();
                    if (needsComma) {
                        writer.write(',');
                    }

                    if (indentFactor > 0) {
                        writer.write('\n');
                    }

                    //indent(writer, newIndent);
                    for (int i = 0; i < newIndent; ++i) {
                        writer.write(' ');
                    }

                    String key = entry.getKey();
                    writer.write(quote(key));
                    writer.write(':');
                    if (indentFactor > 0) {
                        writer.write(' ');
                    }

                    try {
                        writeValue(writer, entry.getValue(), indentFactor, newIndent);
                    } catch (Exception var11) {
                        throw new JSONException("Unable to write JSONObject value for key: " + key, var11);
                    }
                }

                if (indentFactor > 0) {
                    writer.write('\n');
                }

                //indent(writer, indent);
                for (int i = 0; i < indent; ++i) {
                    writer.write(' ');
                }
            }

            writer.write('}');
            return writer;
        } catch (IOException var13) {
            throw new JSONException(var13);
        }
    }

    static Writer writeValue(Writer writer, Object value, int indentFactor, int indent) throws JSONException, IOException {
        if (value != null && !value.equals(null)) {
            String numberAsString;
            if (value instanceof JSONString) {
                try {
                    numberAsString = ((JSONString) value).toJSONString();
                } catch (Exception var6) {
                    throw new JSONException(var6);
                }

                writer.write(numberAsString != null ? numberAsString.toString() : quote(value.toString()));
            } else if (value instanceof Number) {
                numberAsString = numberToString((Number) value);
                if (NUMBER_PATTERN.matcher(numberAsString).matches()) {
                    writer.write(numberAsString);
                } else {
                    quote(numberAsString, writer);
                }
            } else if (value instanceof Boolean) {
                writer.write(value.toString());
            } else if (value instanceof Enum) {
                writer.write(quote(((Enum<?>) value).name()));
            } else if (value instanceof XJSONObject) {
                ((XJSONObject) value).write(writer, indentFactor, indent);
            } else if (value instanceof JSONObject) {
                new XJSONObject(((JSONObject) value).toMap()).write(writer, indentFactor, indent);
            } else if (value instanceof JSONArray) {
                JSONArray a = new JSONArray((JSONArray) value) {
                    @Override
                    public Writer write(Writer writer, int indentFactor, int indent) throws JSONException {
                        try {
                            boolean needsComma = false;
                            int length = this.length();
                            writer.write('[');

                            ArrayList<Object> myArrayList;
                            try {
                                Field f = JSONArray.class.getDeclaredField("myArrayList");
                                f.setAccessible(true);
                                myArrayList = (ArrayList<Object>) f.get(this);
                            } catch (Exception e) {
                                e.printStackTrace();
                                return null;
                            }

                            if (length == 1) {
                                final int newIndent = indent + indentFactor;
                                if (indentFactor > 0) {
                                    writer.write('\n');
                                }
                                //JSONObject.indent(writer, newIndent);

                                for (int ix = 0; ix < newIndent; ix += 1) {
                                    writer.write(' ');
                                }
                                try {
                                    XJSONObject.writeValue(writer, myArrayList.get(0),
                                            indentFactor, newIndent);
                                } catch (Exception e) {
                                    throw new JSONException("Unable to write JSONArray value at index: 0", e);
                                }

                                if (indentFactor > 0) {
                                    writer.write('\n');
                                }
                                //JSONObject.indent(writer, indent);

                                for (int ix = 0; ix < indent; ix += 1) {
                                    writer.write(' ');
                                }
                            } else if (length != 0) {
                                final int newIndent = indent + indentFactor;

                                for (int i = 0; i < length; i += 1) {
                                    if (needsComma) {
                                        writer.write(',');
                                    }
                                    if (indentFactor > 0) {
                                        writer.write('\n');
                                    }
                                    //JSONObject.indent(writer, newIndent);

                                    for (int ix = 0; ix < newIndent; ix += 1) {
                                        writer.write(' ');
                                    }
                                    try {
                                        XJSONObject.writeValue(writer, myArrayList.get(i),
                                                indentFactor, newIndent);
                                    } catch (Exception e) {
                                        throw new JSONException("Unable to write JSONArray value at index: " + i, e);
                                    }
                                    needsComma = true;
                                }
                                if (indentFactor > 0) {
                                    writer.write('\n');
                                }
                                //JSONObject.indent(writer, indent);

                                for (int ix = 0; ix < indent; ix += 1) {
                                    writer.write(' ');
                                }
                            }
                            writer.write(']');
                            return writer;
                        } catch (IOException e) {
                            throw new JSONException(e);
                        }
                    }
                };

                a.write(writer, indentFactor, indent);


            } else if (value instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) value;
                (new XJSONObject(map)).write(writer, indentFactor, indent);
            } else if (value instanceof Collection) {
                Collection<?> coll = (Collection<?>) value;
                (new JSONArray(coll)).write(writer, indentFactor, indent);
            } else if (value.getClass().isArray()) {
                (new JSONArray(value)).write(writer, indentFactor, indent);
            } else {
                quote(value.toString(), writer);
            }
        } else {
            writer.write("null");
        }

        return writer;
    }

}
