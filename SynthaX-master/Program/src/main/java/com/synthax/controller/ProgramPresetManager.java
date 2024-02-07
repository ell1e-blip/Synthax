package com.synthax.controller;

import com.synthax.model.effects.SynthaxLFO;
import net.beadsproject.beads.data.Buffer;

import java.io.*;
import java.util.ArrayList;

public class ProgramPresetManager {

    private final SynthaxController synthaxController;

    private File presetRoot = new File("src/main/resources/com/synthax/program_presets");

    private final static String PRESET_FILE_EXTENSION = ".dat";



    public ProgramPresetManager(SynthaxController synthaxController) {
        this.synthaxController = synthaxController;
    }

    public void savePresetGetFile(String presetName, SynthaxLFO synthaxLFO) {
        if(presetName != null) {
            File saveFile = getFileFromPresetName(presetName);
            savePreset(saveFile, synthaxLFO);
        }
    }

    private File getFileFromPresetName(String presetName) {
        return new File(presetRoot, presetName + PRESET_FILE_EXTENSION);
    }

    public String[] getPresetNames() {
        ArrayList<String> presetNames = new ArrayList<>();

        File[] children = presetRoot.listFiles();
        if(children != null) {
            for(File child : children) {
                if(child.isFile()) {
                    String childName = child.getName();
                    if(childName.endsWith(PRESET_FILE_EXTENSION)) {
                        childName = childName.substring(0, childName.length() - 4);
                        presetNames.add(childName);
                    }
                }
            }
        }

        return presetNames.toArray(new String[0]);
    }

    private void savePreset(File saveFile, SynthaxLFO synthaxLFO) {
        float depthvalue = synthaxLFO.getDepthValue();
        Buffer waveformBuffer = synthaxLFO.getWaveformBuffer();
        float rateFreq = synthaxLFO.getRateFrequency();
        System.out.println("depthvalue: " + depthvalue);
        System.out.println("Buffer:" + waveformBuffer);
        System.out.println("Buffer: " + waveformBuffer.toString());
        System.out.println("rateFreq: " + rateFreq);


        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(saveFile))) {
            dos.writeFloat(depthvalue);
            dos.writeChars(waveformBuffer.toString());
            dos.writeFloat(rateFreq);
            dos.flush();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }

    public void loadPreset(String presetName) {
    }
}
