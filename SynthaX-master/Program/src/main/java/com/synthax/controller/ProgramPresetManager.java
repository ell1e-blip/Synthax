package com.synthax.controller;

import com.synthax.model.effects.SynthaxADSR;
import com.synthax.model.effects.SynthaxLFO;
import com.synthax.model.enums.OctaveOperands;
import com.synthax.model.enums.Waveforms;
import net.beadsproject.beads.data.Buffer;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Ellie Rosander
 * @author Marcus Larsson
 * @author Oliver Berggren
 * Class for saving and loading preset of the program settings
 *
 */
public class ProgramPresetManager {

    private final SynthaxController synthaxController;
    //private final NoiseController noiseController;

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
        //this.noiseController =
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

    /**
     * @author Ellie Rosander
     * @author Marcus Larsson
     * @param saveFile
     */

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

        //ADSR
        float ADSRAttack = synthaxController.getAttackValue();
        float ADSRDecay = synthaxController.getDecayValue();
        float ADSRSustain = synthaxController.getSustainValue();
        float ADSRRelease = synthaxController.getReleaseValue();

        //Noise
        float noise = synthaxController.getNoiseGainValue();

        //Master
        float master = synthaxController.getMasterGainGlide();

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
            //LFO
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

            dos.writeFloat(synthaxController.getHPCutOff());
            dos.writeFloat(synthaxController.getLPCutoff());
            for (int i = 0; i < 3; i++) {
                dos.writeFloat(synthaxController.getEQGain(i));
                dos.writeFloat(synthaxController.getEQFreq(i));
                dos.writeFloat(synthaxController.getEQRange(i));
            }

            //ADSR
            dos.writeFloat(ADSRAttack);
            dos.writeFloat(ADSRDecay);
            dos.writeFloat(ADSRSustain);
            dos.writeFloat(ADSRRelease);

            //Noise
            dos.writeFloat(noise);

            //Master
            dos.writeFloat(master);

            /*
            TODO Presets för oscillator som vi inte hann implementera.

            //Oscillatorer
            ArrayList<OscillatorController> oscillatorControllers = synthaxController.getOscillatorManager().getOscillatorControllers(); //Sparar antalet oscillatorer
            System.out.println("Antal oscillatorer: " + oscillatorControllers.size());
            
            for (OscillatorController oscillatorController : oscillatorControllers) {
                Buffer waveFormOsc = oscillatorController.getWaveform().getBuffer();
                OctaveOperands octave = oscillatorController.getOctave();
                //float gain = TODO gain bugg
                float detune = oscillatorController.getDetuneCent();
                float depth = oscillatorController.getOscDepth();
                float rate = oscillatorController.getOscRate();
                
                dos.writeInt(oscillatorControllers.size());

                dos.writeInt(waveFormOsc.buf.length);
                for (float value : waveFormOsc.buf) {
                    dos.writeFloat(value);
                }


                dos.writeInt(octave.getOperandValue());
                //dos.writeFloat(gain);
                dos.writeFloat(detune);
                dos.writeFloat(depth);
                dos.writeFloat(rate);
            }

            //TEST:
            for (OscillatorController oscillatorController : oscillatorControllers) {
                System.out.println("****** TEST ******");
                System.out.println("WaveForm: " + oscillatorController.getWaveform());
                System.out.println("Octave: " + oscillatorController.getOctave().getOperandValue());
                System.out.println("Detune: " + oscillatorController.getDetuneCent());
                System.out.println("Depth: " + oscillatorController.getOscDepth());
                System.out.println("Rate: " + oscillatorController.getOscRate());
            }

             */

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

    /**
     * @author Ellie Rosander
     * @author Marcus Larsson
     * @param loadFile
     */
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

            float hpCutoff = dis.readFloat();
            float lpCutoff = dis.readFloat();
            for (int i = 0; i < 3; i++) {
                synthaxController.setViewEQGain(i, dis.readFloat());
                synthaxController.setViewEQFreq(i, dis.readFloat());
                synthaxController.setViewEQRange(i, dis.readFloat());
            }

            Float ADSRAttack = dis.readFloat();
            Float ADSRDecay = dis.readFloat();
            Float ADSRSustain = dis.readFloat();
            Float ADSRRelease = dis.readFloat();

            //Noise
            Float noise = dis.readFloat();
            System.out.println("noiseAfter: " + noise);

            //Master
            Float master = dis.readFloat();


            /*
            TODO Presets för oscillator som vi inte hann implementera.

            //Oscillator
            int nbrOfOscillators = dis.readInt();
            //int oscWaveFormSize = dis.readInt();
            System.out.println("LOAD - nbrOfOscillators: " + nbrOfOscillators);

            for (int i = 0; i < nbrOfOscillators; i++) {


                for (int j = 0; j < oscWaveFormSize; j++) {
                    float oscWave = dis.readFloat();
                }



                int octave = dis.readInt();
                System.out.println("LOAD - Octave: " + octave);
                //Float gain = dis.readFloat
                Float detune = dis.readFloat();
                System.out.println("LOAD - Detune: " + detune);
                Float depth = dis.readFloat();
                System.out.println("LOAD - Depth: " + depth);
                Float rate = dis.readFloat();
                System.out.println("LOAD - Rate: " + rate);

                OscillatorController osc = new OscillatorController(octave);
                osc.setWaveform(Waveforms.SINE);
                //gain
                osc.setDetuneCent(detune);
                osc.setLFODepth(depth);
                osc.setLFORate(rate);
                synthaxController.addOscillator(osc);
            }

             */


            dis.close();


            synthaxController.setViewLFODepth(depthvalue);
            synthaxController.setViewLFORate(knobRate);
            synthaxController.setViewLFOBuffer(waveformBuffer);

            synthaxController.setViewReverbSize(reverbSize);
            synthaxController.setViewReverbTone(reverbTone);
            synthaxController.setViewReverbAmount(reverbAmount);

            synthaxController.updateDelayView(delayTime, delayDecay, delayLevel, delayFeedback);

            synthaxController.setViewHPCutoff(hpCutoff);
            synthaxController.setViewLPCutoff(lpCutoff);

            synthaxController.setViewASDRSliderAttack(ADSRAttack);
            synthaxController.setViewASDRSliderDecay(ADSRDecay);
            synthaxController.setViewASDRSliderSustain(ADSRSustain);
            synthaxController.setViewASDRSliderRelease(ADSRRelease);

            synthaxController.setNoise(noise);

            synthaxController.setMasterGain(master);

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

    /**
     * @author Ellie Rosander
     * @author Oliver Berggren
     * @param mappedFeedback
     */
    public void setDelayFeedback(float mappedFeedback) {
        System.out.println(mappedFeedback + " feedback in PPM");
        this.delayFeedback = mappedFeedback;
    }

    /**
     * @author Ellie Rosander
     * @author Oliver Berggren
     * @param mappedTime
     */
    public void setDelayTime(float mappedTime) {
        this.delayTime = mappedTime;
    }
    /**
     * @author Ellie Rosander
     * @author Oliver Berggren
     * @param decayValue
     */

    public void setDelayDecay(float decayValue) {
        this.delayDecay = decayValue;
    }

    /**
     * @author Ellie Rosander
     * @author Oliver Berggren
     * @param levelValue
     */
    public void setDelayLevel(float levelValue) {
        this.delayLevel = levelValue;
    }


}
