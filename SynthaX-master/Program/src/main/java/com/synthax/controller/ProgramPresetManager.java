package com.synthax.controller;

import com.synthax.model.effects.SynthaxLFO;
import com.synthax.model.enums.Waveforms;
import net.beadsproject.beads.data.Buffer;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * @author Ellie Rosander
 * Class for saving and loading preset of the program settings
 *
 */
public class ProgramPresetManager {

    private final SynthaxController synthaxController;

    private final static int PRESET_VERSION_ID = 2;
    private final static int PRESET_UID = 133769421;

    private File presetRoot = new File("src/main/resources/com/synthax/program_presets");

    private final static String PRESET_FILE_EXTENSION = ".dat";





    public ProgramPresetManager(SynthaxController synthaxController) {
        this.synthaxController = synthaxController;
    }

    public void savePresetGetFile(String presetName) {
        if(presetName != null) {
            File saveFile = getFileFromPresetName(presetName);
            savePreset(saveFile);
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

    private void savePreset(File saveFile) {
       // Boolean active = synthaxController.getLFOActive();
      //  if(active) {

            //LFO
            float depthvalue = synthaxController.getLFODepth();
            Buffer waveformBuffer = synthaxController.getLFOWaveForm();
            float rateFreq = synthaxController.getLFOrate();
            float phase = synthaxController.getLFOPhase();
            float knobRate = synthaxController.getViewLFORate();

            System.out.println("ProgramPresetManager/savePreset: depthvalue  " +depthvalue);
            System.out.println("ProgramPresetManager/savePreset: waveformBuffer  " +waveformBuffer);
            System.out.println("ProgramPresetManager/savePreset: rateFreq  " +rateFreq);
            System.out.println("ProgramPresetManager/savePreset: phase  " +phase);
            System.out.println("ProgramPresetManager/savePreset: knobRate  " +knobRate);

            //DELAY
            float delayFeedback = synthaxController.getDelayFeedback();
            float delayTime = synthaxController.getDelayTime();
            float delayDecay = synthaxController.getDelayDecay();
            float delayLevel = synthaxController.getDelayLevel();

            System.out.println("ProgramPresetManager/savePreset: Feedback  " +delayFeedback);
            System.out.println("ProgramPresetManager/savePreset: Time  " +delayTime);
            System.out.println("ProgramPresetManager/savePreset: Decay  " +delayDecay);
            System.out.println("ProgramPresetManager/savePreset: Level  " +delayLevel);

           // float knob
            //TODO kolla knobsen
            /*
            System.out.println("depthvalue: " + depthvalue);
            System.out.println("Buffer: " + waveformBuffer.toString());
            System.out.println("rateFreq: " + rateFreq);
            System.out.println("knobRate" + knobRate);
            */

            try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(saveFile))) {
                dos.writeInt(PRESET_UID);
                dos.writeInt(PRESET_VERSION_ID);

                //LFO SETTINGS
                dos.writeFloat(depthvalue);
                int bufferSize = waveformBuffer.buf.length;
                dos.writeInt(bufferSize);
                // Save each float value
                for (float value : waveformBuffer.buf) {
                    dos.writeFloat(value);
                }
                dos.writeFloat(rateFreq);
                dos.writeFloat(phase);
                dos.writeFloat(knobRate);

                //DELAY SETTINGS
                dos.writeFloat(delayFeedback);
                dos.writeFloat(delayTime);
                dos.writeFloat(delayDecay);
                dos.writeFloat(delayLevel);


                System.out.println("ProgramPresetManager/savePreset: Feedback  " +delayFeedback);
                System.out.println("ProgramPresetManager/savePreset: Time  " +delayTime);
                System.out.println("ProgramPresetManager/savePreset: Decay  " +delayDecay);
                System.out.println("ProgramPresetManager/savePreset: Level  " +delayLevel);

                dos.flush();

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

    }

    /**
     * author Ellie Rosander
     * @param presetName
     */
    public void loadPreset(String presetName) {
        System.out.println(presetName + "IN LOAD PRESET METHOD");
        File presetToLoad = getFileFromPresetName(presetName);
        boolean loadOk = false;

        File[] children = presetRoot.listFiles();
        if(children != null) {
            for(File child : children) {
                if(child.equals(presetToLoad)) {
                    loadPreset(presetToLoad);
                    loadOk = true;
                    break;
                }
            }
        }

        if(!loadOk) {
            System.err.println("SeqPresetLoader.loadPreset(String): Preset with matching name " + presetName + " not found.");
        }
    }

    private void loadPreset(File loadFile) {
        System.out.println("LOADING: " + loadFile);


        try (DataInputStream dis = new DataInputStream(new FileInputStream(loadFile))){
            int presetUID = dis.readInt();
            if(presetUID != PRESET_UID) {
                System.err.println("Not a valid preset.");
                return;
            }

            int presetVersion = dis.readInt();
            if(presetVersion != PRESET_VERSION_ID) {
                System.err.println("Preset not correct version.");
                return;
            }

            //LFO SETTINGS
            float depthvalue = dis.readFloat();

            int bufferSize = dis.readInt();

            // Create a float array to store the loaded data
            float[] bufferData = new float[bufferSize];

            // Read each float value into the buffer data array
            for (int i = 0; i < bufferSize; i++) {
                bufferData[i] = dis.readFloat();
            }

            // Reconstruct the Buffer from the loaded data
            Buffer waveformBuffer = new Buffer(bufferData.length);

            try {
                Field bufField = Buffer.class.getDeclaredField("buf");
                bufField.setAccessible(true);
                bufField.set(waveformBuffer, bufferData);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
                // Handle the exception appropriately
            }
            //TODO check if Float is correct or float?
            Float rateFreq = dis.readFloat();
            Float phase = dis.readFloat();
            Float knobRate = dis.readFloat();

            //DELAY SETTINGS
            float delayFeedback = dis.readFloat();
            float delayTime = dis.readFloat();
            float delayDecay = dis.readFloat();
            float delayLevel = dis.readFloat();

            // Update SynthaxDelay with loaded parameters
            synthaxController.updateDelayParameters(delayFeedback, delayTime, delayDecay, delayLevel);

            dis.close();

            /*
            System.out.println(depthvalue);
            System.out.println(waveformBuffer.toString());
            System.out.println(rateFreq);
            System.out.println(knobRate);
             */

            System.out.println("ProgramPresetManger/loadPreset: Feedback  " +delayFeedback);
            System.out.println("ProgramPresetManger/loadPreset: Time  " +delayTime);
            System.out.println("ProgramPresetManger/loadPreset: Decay  " +delayDecay);
            System.out.println("ProgramPresetManger/loadPreset: Level  " +delayLevel);


           // synthaxController.setLFODepth(depthvalue);
           // synthaxController.setLFOBuffer(waveformBuffer);
            //synthaxController.setLFORate(rateFreq);
            //synthaxController.setLFOPhase(phase);

            // Apply Delay settings to the model
         /**   synthaxController.setDelayFeedback(delayFeedback);
            synthaxController.setDelayTime(delayTime);
            synthaxController.setDelayDecay(delayDecay);
            synthaxController.setDelayLevel(delayLevel);*/

            synthaxController.setViewLFODepth(depthvalue);
            synthaxController.setViewLFORate(knobRate);
            synthaxController.setViewLFOBuffer(waveformBuffer);

            //SET DELAY VIEW
            synthaxController.setViewDelayFeedback(delayFeedback);
            synthaxController.setViewDelayTime(delayTime);
            synthaxController.setViewDelayDecay(delayDecay);
            synthaxController.setViewDelayLevel(delayLevel);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
