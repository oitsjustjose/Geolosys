//package com.oitsjustjose.geolosys.client.patchouli.processors;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.URL;
//
//import vazkii.patchouli.api.IComponentProcessor;
//import vazkii.patchouli.api.IVariable;
//import vazkii.patchouli.api.IVariableProvider;
//
//public class PatronProcessor implements IComponentProcessor {
//
//    String patrons = "";
//
//    @Override
//    public void setup(IVariableProvider variables) {
//        try {
//            URL url = new URL("https://patrons.geolosys.com/");
//            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
//
//            String line;
//            StringBuilder allData = new StringBuilder();
//            while ((line = in.readLine()) != null) {
//                if (!line.isEmpty()) {
//                    allData.append(line);
//                }
//            }
//            in.close();
//
//            patrons = allData.toString();
//        } catch (IOException e) {
//            patrons = "Failed to grab patrons from API";
//        }
//    }
//
//    @Override
//    public IVariable process(String key) {
//        if (key.equals("patrons")) {
//            return IVariable.wrap(patrons);
//        }
//        return null;
//    }
//}
