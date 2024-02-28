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
    private float delayFeedback;
    private float delayTime;
    private float delayDecay;
    private float delayLevel;


    public ProgramPresetManager(SynthaxController synthaxController) {
        this.synthaxController = synthaxController;
    }

    public void savePresetGetFile(String presetName) {
        System.out.println("test");
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
        System.out.println("test2");


            float depthvalue = synthaxController.getLFODepth();
            Buffer waveformBuffer = synthaxController.getLFOWaveForm();
            float rateFreq = synthaxController.getLFOrate();
            float phase = synthaxController.getLFOPhase();
            float knobRate = synthaxController.getViewLFORate();

            //Fortsätter med reverb
            float reverbSize = synthaxController.getReverbSize();
            float reverbTone = synthaxController.getReverbTone();
            float reverbAmount = synthaxController.getReverbAmount();

            /*
            System.out.println("depthvalue: " + depthvalue);
            System.out.println("Buffer: " + waveformBuffer.toString());
            System.out.println("rateFreq: " + rateFreq);
            System.out.println("knobRate" + knobRate);
            */

            try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(saveFile))) {
                dos.writeInt(PRESET_UID);
                dos.writeInt(PRESET_VERSION_ID);
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
                //Reverb
                dos.writeFloat(reverbSize);
                dos.writeFloat(reverbTone);
                dos.writeFloat(reverbAmount);
                //Delay
                dos.writeFloat(delayTime);
                dos.writeFloat(delayDecay);
                dos.writeFloat(delayLevel);
                dos.writeFloat(delayFeedback);

                for (int i = 0; i < 3; i++) {
                    dos.writeFloat(synthaxController.getEQGain(i));
                    dos.writeFloat(synthaxController.getEQFreq(i));
                    dos.writeFloat(synthaxController.getEQRange(i));
                }
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
            Float rateFreq = dis.readFloat();
            Float phase = dis.readFloat();
            Float knobRate = dis.readFloat();

            //Continue with reverb
            Float reverbSize = dis.readFloat();
            Float reverbTone = dis.readFloat();
            Float reverbAmount = dis.readFloat();
            //Delay
            delayTime = dis.readFloat();
            delayDecay = dis.readFloat();
            delayLevel = dis.readFloat();
            delayFeedback = dis.readFloat();

            for (int i = 0; i < 3; i++) {
                synthaxController.setViewEQGain(i, dis.readFloat());
                synthaxController.setViewEQFreq(i, dis.readFloat());
                synthaxController.setViewEQRange(i, dis.readFloat());
            }

            dis.close();

            synthaxController.setViewLFODepth(depthvalue);
            synthaxController.setViewLFORate(knobRate);
            synthaxController.setViewLFOBuffer(waveformBuffer);

            synthaxController.setViewReverbSize(reverbSize);
            synthaxController.setViewReverbTone(reverbTone);
            synthaxController.setViewReverbAmount(reverbAmount);

            synthaxController.updateDelayView(delayTime, delayDecay, delayLevel, delayFeedback);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteFile(String presetName) {
        if(presetName != null) {
            File[] children = presetRoot.listFiles();
            if(children != null) {
                File deleteFile = getFileFromPresetName(presetName);

                for (File child : children) {
                    if(child.equals(deleteFile)) {
                        if(deleteFile.delete()) {
                            System.out.println(presetName + " deleted.");
                        } else {
                            System.err.println(presetName + " could not be deleted.");
                        }
                    }
                }
            } else {
                System.err.println("SeqPresetLoader.deletePreset(): Root has no children.");
            }
        }
    }

    public void setDelayFeedback(float mappedFeedback) {
        System.out.println(mappedFeedback + " feedback in PPM");
        this.delayFeedback = mappedFeedback;
    }

    public void setDelayTime(float mappedTime) {
        this.delayTime = mappedTime;
    }

    public void setDelayDecay(float decayValue) {
        this.delayDecay = decayValue;
    }

    public void setDelayLevel(float levelValue) {
        this.delayLevel = levelValue;
    }
}
