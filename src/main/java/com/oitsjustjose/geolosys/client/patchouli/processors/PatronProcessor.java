package com.oitsjustjose.geolosys.client.patchouli.processors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

public class PatronProcessor implements IComponentProcessor {

    private static IVariable patrons = IVariable.wrap("Haven't grabbed patrons from API");

    public static void fetchPatrons() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                URL url = new URL("https://patrons.geolosys.com/");
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

                String line;
                StringBuilder allData = new StringBuilder("$(l)A huge thank you to: $()$(br2)");
                while ((line = in.readLine()) != null) {
                    if (!line.isEmpty()) {
                        allData.append(line);
                    }
                }
                in.close();
                return allData.toString();
            } catch (IOException e) {
                return "Failed to grab patrons from API";
            }
        });

        future.thenAccept((d) -> {
            patrons = IVariable.wrap(d);
        });

    }

    @Override
    public void setup(IVariableProvider variables) {
        // No setup required, fetchPatrons is ran from Geolosys main on startup
    }

    @Override
    public IVariable process(String key) {
        if (key.equals("patrons")) {
            return patrons;
        }
        return null;
    }
}
