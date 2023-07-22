package com.example.finalproject;

import android.content.Context;
import android.content.res.AssetManager;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ProductXMLParser {
    final static String KEY_PRODUCT="product";
    final static String KEY_NAME="name";
    final static String KEY_BRAND="brand";
    final static String KEY_DETAILS="details";
    final static String KEY_PRICE="price";
    final static String KEY_IMAGE="image";



    public static ArrayList<Product> parseProducts(Context context){
        ArrayList<Product> data = null;
        InputStream in = openProductsFile(context);
        XmlPullParserFactory xmlFactoryObject;
        try {
            xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser parser = xmlFactoryObject.newPullParser();

            parser.setInput(in, null);
            int eventType = parser.getEventType();
            Product currentProduct = null;
            String inTag = "";
            String strTagText = null;

            while (eventType != XmlPullParser.END_DOCUMENT){
                inTag = parser.getName();
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        data = new ArrayList<Product>();
                        break;
                    case XmlPullParser.START_TAG:
                        if (inTag.equalsIgnoreCase(KEY_PRODUCT))
                            currentProduct = new Product();
                        break;
                    case XmlPullParser.TEXT:
                        strTagText = parser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (inTag.equalsIgnoreCase(KEY_PRODUCT))
                            data.add(currentProduct);
                        else if (inTag.equalsIgnoreCase(KEY_NAME))
                            currentProduct.name = strTagText;
                        else if (inTag.equalsIgnoreCase(KEY_BRAND))
                            currentProduct.brand =strTagText;
                        else if (inTag.equalsIgnoreCase(KEY_DETAILS))
                            currentProduct.setDetails(strTagText);
                        else if(inTag.equalsIgnoreCase(KEY_PRICE))
                            currentProduct.price = Double.valueOf(strTagText);
                        else if(inTag.equalsIgnoreCase(KEY_IMAGE))
                            currentProduct.image = strTagText;
                        inTag ="";
                        break;
                }//switch
                eventType = parser.next();
            }//while
        } catch (Exception e) {e.printStackTrace();}
        return data;
    }

    private static InputStream openProductsFile(Context context){
        AssetManager assetManager = context.getAssets();
        InputStream in =null;
        try {
            in = assetManager.open("products.xml");
        } catch (IOException e) {e.printStackTrace();}
        return in;
    }
}
