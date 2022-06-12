package com.mrshiehx.cmcl.utils.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class XJSONTokener extends JSONTokener {
    public XJSONTokener(String s) {
        super(s);
    }

    public Object nextValue() throws JSONException {
        char c = this.nextClean();
        switch (c) {
            case '"':
            case '\'':
                return this.nextString(c);
            case '[':
                this.back();

                try {
                    return new JSONArray(this) {
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
                } catch (StackOverflowError var5) {
                    throw new JSONException("JSON Array or Object depth too large to process.", var5);
                }
            case '{':
                this.back();

                try {
                    return new XJSONObject(this);
                } catch (StackOverflowError var4) {
                    throw new JSONException("JSON Array or Object depth too large to process.", var4);
                }
            default:
                StringBuilder sb;
                for (sb = new StringBuilder(); c >= ' ' && ",:]}/\\\"[{;=#".indexOf(c) < 0; c = this.next()) {
                    sb.append(c);
                }

                try {
                    Field f = JSONTokener.class.getDeclaredField("eof");
                    f.setAccessible(true);
                    if (!f.getBoolean(this)) {
                        this.back();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String string = sb.toString().trim();
                if ("".equals(string)) {
                    throw this.syntaxError("Missing value");
                } else {
                    return XJSONObject.stringToValue(string);
                }
        }
    }

}
