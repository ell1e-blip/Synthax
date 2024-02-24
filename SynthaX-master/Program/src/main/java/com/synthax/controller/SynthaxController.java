package com.synthax.controller;

import com.synthax.model.effects.SynthaxDelay;
import com.synthax.model.effects.SynthaxEQFilters;
import com.synthax.model.midi.Midi;
import com.synthax.model.effects.SynthaxLFO;
import com.synthax.model.effects.SynthaxReverb;
import com.synthax.model.enums.MidiNote;
import com.synthax.model.enums.Waveforms;
import com.synthax.model.sequencer.Sequencer;
import com.synthax.model.enums.SequencerMode;
import com.synthax.model.sequencer.SequencerStep;
import com.synthax.view.SynthaxView;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.io.JavaSoundAudioIO;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.*;

import java.util.Random;

/**
 * Main controller for the synthesizer
 * Handles audio chain setup, internal structure and forwarding messages to different parts of the application
 * @author Joel Eriksson Sinclair
 * @author Luke Eales
 * @author Axel Nilsson
 * @author Teodor Wegestål
 * @author Viktor Lenberg
 */
public class SynthaxController {

    public static final float MINIMUM_GLIDE_DURATION = 10f;

    private final SynthaxView synthaxView;
    private final SeqPresetLoader seqPresetLoader;

    private final ProgramPresetManager programPresetManager;

    private final Glide masterGainGlide;
    private final SynthaxLFO synthaxLFO;
    private final SynthaxDelay synthaxDelay;
    private final OscillatorManager oscillatorManager;
    private final SynthaxEQFilters filters;
    private final Sequencer sequencer;
    private final SynthaxReverb reverb;
    private boolean randomFreq = true;
    private boolean randomGain = true;
    private boolean randomOnOff = true;
    private final Midi midi;

    /**
     * Setup AudioContext, OscillatorManager and defines the chain of effects and sounds
     * before sending it to the output/system speakers
     * */
    public SynthaxController(SynthaxView synthaxView) {
        this.synthaxView = synthaxView;
        JavaSoundAudioIO jsaio = new JavaSoundAudioIO(512);
        AudioContext ac = new AudioContext(jsaio);
        AudioContext.setDefaultContext(ac);

        sequencer = new Sequencer(this);
        seqPresetLoader = new SeqPresetLoader(sequencer);

        programPresetManager = new ProgramPresetManager(this);


        masterGainGlide = new Glide(ac, 0.5f, 50);
        Gain masterGain = new Gain(ac, 1, masterGainGlide);

        oscillatorManager = OscillatorManager.getInstance();
        Gain oscCombined = oscillatorManager.getFinalOutput();

        synthaxLFO = new SynthaxLFO();
        synthaxLFO.setInput(oscCombined);


        filters = new SynthaxEQFilters();
        filters.addInput(synthaxLFO.getOutput());

        reverb = new SynthaxReverb(filters.getOutput());
        masterGain.addInput(reverb.getOutput());

        synthaxDelay = new SynthaxDelay(filters.getOutput());
        masterGain.addInput(synthaxDelay.getOutput());


        // Send to audio-device
        ac.out.addInput(masterGain);
        ac.start();

        midi = new Midi(this);
        synthaxView.updateMidiLabel(midi.connectMidi());
    }

    //region OscillatorManager (click to open/collapse)
    public void moveOscillatorDown(OscillatorController oscillatorController) {
        oscillatorManager.moveOscillatorDown(oscillatorController);
    }

    public Gain getLFOoutput() {
        return synthaxLFO.getOutput();
    }


    public void moveOscillatorUp(OscillatorController oscillatorController) {
        oscillatorManager.moveOscillatorUp(oscillatorController);
    }

    public void addOscillator(OscillatorController oscillatorController) {
        oscillatorManager.addOscillator(oscillatorController);
    }

    public void removeOscillator(OscillatorController oscillatorController) {
        oscillatorManager.removeOscillator(oscillatorController);
    }
    //endregion OscillatorManager

    //region MIDI-handling (click to open/collapse)
    public void noteOn(MidiNote midiNote, int velocity) {
        oscillatorManager.noteOn(midiNote, velocity);
    }

    public void noteOn(MidiNote midiNote, int velocity, float detuneCent) {
        oscillatorManager.noteOn(midiNote, velocity, detuneCent);
    }
    public void noteOff(MidiNote midiNote){
        oscillatorManager.noteOff(midiNote);
    }
    //endregion MIDI-handling

    //region Filters (click to open/collapse)
    public void setHPCutoff(float cutoff) {
        filters.setHPCutoff(cutoff);
    }

    public void setHPActive() {
        filters.setHPActive();
    }

    public void setEQActive(int i) {
        filters.setEQActive(i);
    }

    public void setEQGain(int i, float newVal) {
        filters.setEQGain(i, newVal);
    }

    public void setEQFreq(int i, float newVal) {
        filters.setEQFrequency(i, newVal);
    }

    public void setEQRange(int i, float newVal) {
        filters.setEQRange(i, newVal);
    }

    public void setLPCutoff(float cutoff) {
        filters.setLPCutoff(cutoff);
    }

    public void setLPActive() {
        filters.setLPActive();
    }
    //endregion Filters

    //region LFO (click to open/collapse)

    public void setLFOWaveform(Waveforms waveform) {
        synthaxLFO.setWaveform(waveform);
    }



    public void setLFOActive() {
        synthaxLFO.setActive();
    }
    //endregion LFO

    //region Noise (click to open/collapse)
    public void setNoiseGain(float gain) {
        oscillatorManager.getNoiseController().setGain(gain);
    }

    public void setNoiseActive() {
        oscillatorManager.getNoiseController().setActive();
    }
    //endregion Noise

    //region Sequencer (click to open/collapse)
    public void setStepOnOff(int i, boolean on) {
        sequencer.setOnOff(i, on);
    }

    public void setSequencerMode(SequencerMode sequencerMode) {
        sequencer.setSequencerMode(sequencerMode);
    }

    public boolean sequencerIsRunning() {
        return sequencer.isRunning();
    }

    public void sequencerOn() {
        sequencer.start();
    }

    public void sequencerOff() {
        sequencer.stop();
    }

    public void setSeqMidiNote(int i, MidiNote midiNote) {
        sequencer.setStepMidiNote(i, midiNote);
    }

    public void setSeqDetuneCent(int i, float detuneCent) {
        sequencer.setStepDetuneCent(i, detuneCent);
    }

    public void setSeqVelocity(int i, float velocity) {
        sequencer.setStepVelocity(i, velocity);
    }

    public void setSeqBPM(float rate) {
        sequencer.setBPM(rate);
    }

    public void setSeqNSteps(int nSteps) {
        sequencer.setNSteps(nSteps);
    }

    public void setSeqButtonOrange(int i) {
        synthaxView.setSeqButtonOrange(i);
    }

    public void setSeqButtonGray(int i) {
        synthaxView.setSeqButtonGray(i);
    }

    public void setRandomFreq(boolean randomFreq) {
        this.randomFreq = randomFreq;
    }

    public void setRandomGain(boolean randomGain) {
        this.randomGain = randomGain;
    }

    public void setRandomOnOff(boolean randomOnOff) {
        this.randomOnOff = randomOnOff;
    }

    public void randomize(int length) {
        if (randomFreq) {
            randomizeFreq(length);
        }
        if (randomOnOff) {
            randomizeOnOff(length);
        }
        if (randomGain) {
            randomizeGain(length);
        }
    }
    public void randomizeOnOff(int length) {
        Random random  = new Random();
        for (int i = 0; i < length; i++) {
            int onOff = random.nextInt(2);
            synthaxView.setSequencerStepOnOff(onOff == 0, i);
        }
    }
    public void randomizeFreq(int length) {
        Random random  = new Random();
        for (int i = 0; i < length; i++) {
            int out = 0;
            for (int j = 0; j < 4; j++) {
                out += random.nextInt(88);
            }
            out /= 4;
            out += 21;
            synthaxView.setSequencerStepFreq(MidiNote.values()[out], i);
        }
    }
    public void randomizeGain(int length) {
        Random random  = new Random();
        for (int i = 0; i < length; i++) {
            int odds = random.nextInt(100);
            if (odds < 70) {
                synthaxView.setSequencerStepGain(1, i);
            } else {
                int b = random.nextInt(100);
                float f = b / 100f;
                synthaxView.setSequencerStepGain(f, i);
            }
        }
    }

    public void updateSeqStepGUI(int i, boolean isOn, int velocity, float detuneCent, MidiNote midiNote) {
        synthaxView.updateSeqStep(i, isOn, velocity, detuneCent, midiNote);
    }

    //region SequencerPreset (click to expand/collapse)
    public void savePresetAsNew(String presetName) {
        presetName = seqPresetLoader.getIndexedName(presetName);
        savePreset(presetName);
    }

    private boolean waitForSequencerToStop(int timeout, String errorMessage) {
        Thread sequencerThread = sequencer.getThread();
        if(sequencerThread != null) {
            synthaxView.fakeSequencerStartStopClick();
            try {
                sequencerThread.join(timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(sequencerThread != null && sequencerThread.isAlive()) {
            System.err.println(errorMessage);
            return false;
        }
        return true;
    }

    public void savePreset(String presetName) {
        // delegate the preset-loading to separate thread
        Thread saver = new Thread(() -> {
            // If sequencer is playing, stop it and do the loading after
            waitForSequencerToStop(250, "CANT SAVE WHILE SEQUENCER IS RUNNING!");

            seqPresetLoader.savePreset(presetName);
            updateSequencerPresetList(presetName);
        });
        saver.start();
    }

    public void onSavePreset(String presetName) {
        // Is there a conflict?
        boolean presetNameConflict = seqPresetLoader.presetExists(presetName);
        if(presetNameConflict) {
            synthaxView.showPresetSaveConflictPopup(presetName);
        } else {
            savePreset(presetName);
        }
    }

    public void onSelectPreset(String presetName) {
        if(presetName == null || presetName.equals("")) {
            return;
        }

        Thread loader = new Thread(() -> {
            // If sequencer is playing, stop it and do the loading after
            boolean stopSuccessful = waitForSequencerToStop(250, "CANT LOAD WHILE SEQUENCER IS RUNNING!");

            if(stopSuccessful) {
                seqPresetLoader.loadPreset(presetName);
                updateSequencerGUI();
                updateSequencerStepsGUI();
            }
        });
        loader.start();
    }

    private void updateSequencerGUI() {
        synthaxView.setSeqKnobRate(sequencer.getRate());
        synthaxView.setSequencerMode(sequencer.getSequencerMode());
        synthaxView.setSequencerNSteps(sequencer.getNSteps());
    }

    private void updateLFOGUI() {
        synthaxView.setKnobLFODepth(synthaxLFO.getDepthValue());
    }

    public void updateSequencerPresetList(String chosenPreset) {
        String[] presetNames = seqPresetLoader.getPresetNames();
        synthaxView.setSequencerPresetList(presetNames, chosenPreset);
    }


    public void updateSequencerPresetList() {
        updateSequencerPresetList("");
    }

    public String[] getSequencerPresetList() {
        return seqPresetLoader.getPresetNames();
    }

    private void updateSequencerStepsGUI() {
        SequencerStep[] steps = sequencer.getSteps();
        for (int i = 0; i < steps.length; i++) {
            SequencerStep step = steps[i];
            updateSeqStepGUI(i, step.isOn(), step.getVelocity(), step.getDetuneCent(), step.getMidiNote());
        }
    }
    //endregion sequencer presets
    //endregion sequencer

    //region Delay (click to open/collapse)
    public void setDelayFeedback(float feedBackDuration) {
        oscillatorManager.setDelayFeedback(feedBackDuration);
    }

    public void setDelayTime(float delayTime) {
        oscillatorManager.setDelayTime(delayTime);
    }

    public void setDelayDecay(float decayValue) {
        oscillatorManager.setDelayDecay(decayValue);
    }

    public void setDelayLevel(float levelValue) {
        oscillatorManager.setDelayLevel(levelValue);
    }

    public void setDelayActive() {
        oscillatorManager.setDelayActive();
        synthaxDelay.setActive();
    }
    //endregion Delay
    //region Reverb (click to open/collapse)
    public void setReverbActive() {
        reverb.setActive();
    }

    public void startRickRoll() {
        sequencer.startRickRoll();
    }

    public void setUpSteps(int i) {
        synthaxView.setUpSteps(i);
    }

    public void setReverbAmount(float amount) {
        reverb.setReverbAmount(amount);
    }

    public void setReverbSize(float size) {
        reverb.setReverbSize(size);
    }

    public void setReverbTone(float tone) {
        reverb.setReverbTone(tone);
    }
    // endregion Reverb
    public void setMasterGain(float gain) {
        masterGainGlide.setValue(gain);
    }

    public boolean connectMidi() {
        return midi.connectMidi();
    }

    public void updateMidiLabel(boolean visable) {
        synthaxView.updateMidiLabel(visable);
    }

    public void deletePreset(String text) {
        seqPresetLoader.deleteFile(text);
    }

    public boolean midiConnected() {
        return midi.midiConnected();
    }

    public Thread getSequencerThread() {
        return sequencer.getThread();
    }

    public void setOscVoiceCount(int voiceCount) {
        Thread voiceChanger = new Thread(() -> {
            // If sequencer is playing, stop it and do the loading after
            boolean stopSuccessful = waitForSequencerToStop(250, "CANT LOAD WHILE SEQUENCER IS RUNNING!");

            if(stopSuccessful) {
                VoiceController.MONOPHONIC_STATUS = false;
                VoiceController.VOICE_COUNT = voiceCount;
                oscillatorManager.setOscillatorVoiceCount(voiceCount);
            }
        });
        voiceChanger.start();
    }

    public void setMonophonic() {
        Thread voiceChanger = new Thread(() -> {
            // If sequencer is playing, stop it and do the loading after
            boolean stopSuccessful = waitForSequencerToStop(250, "CANT LOAD WHILE SEQUENCER IS RUNNING!");

            if(stopSuccessful) {
                VoiceController.MONOPHONIC_STATUS = true;
                oscillatorManager.setOscillatorVoiceCount(0);
            }
        });
        voiceChanger.start();
    }

    /**
     * author Ellie Rosander
     * @param presetname
     */
    public void onSavePresetTest(String presetname) {
        /**boolean presetNameConflict = seqPresetLoader.presetExists(presetName);
        if(presetNameConflict) {
            synthaxView.showPresetSaveConflictPopup(presetName);
        } else {
            savePreset(presetName);
        }**/
        programPresetManager.savePresetGetFile(presetname);
        updateProgramPresetList(presetname);
    }

    /**
     * author Ellie Rosander
     * @param chosenPreset
     */
    public void updateProgramPresetList(String chosenPreset) {
        String[] presetNames = programPresetManager.getPresetNames();
        System.out.println(presetNames);
        synthaxView.setProgramPresetList(presetNames, chosenPreset);
    }

    /**
     * author Ellie Rosander
     */
    public void updateProgramPresetList() {
        updateProgramPresetList("");
    }

    /**
     * author Ellie Rosander
     * vet inte om behövs, kan ev tas bort
     * @return
     */
    public String[] getProgramPresetList() {
        return programPresetManager.getPresetNames();
    }

    /**
     * author Ellie Rosander
     * @param presetName
     */
    public void onSelectProgramPreset(String presetName) {
        if(presetName == null || presetName.equals("")) {
            return;
        }

        Thread loader = new Thread(() -> {
            // If sequencer is playing, stop it and do the loading after
            boolean stopSuccessful = waitForSequencerToStop(250, "CANT LOAD WHILE SEQUENCER IS RUNNING!");

            if(stopSuccessful) {
                programPresetManager.loadPreset(presetName);

            }
        });
        loader.start();
    }

    /**
     * author Ellie Rosander
     * @param depth
     */

    public void setLFODepth(float depth) {
        synthaxLFO.setDepth(depth);
    }

    /**
     * author Ellie Rosander
     * @param depthvalue
     */
    public void setViewLFODepth(float depthvalue) {
        synthaxView.setKnobLFODepth(depthvalue);
    }

    /**
     * author Ellie Rosander
     * @param rate
     */
    public void setLFORate(float rate) {
        synthaxLFO.setRate(rate);
    }

    /**
     * author Ellie Rosander
     * @return
     */
    public float getViewLFORate() {
        return synthaxView.getKnobLFORate();
    }

    /**
     * author Ellie Rosander
     * @param rateFreq
     */
    public void setViewLFORate(Float rateFreq) {
        synthaxView.setKnobLFORate(rateFreq);
    }

    /**
     * author Ellie Rosander
     * @param waveformBuffer
     */

    public void setLFOBuffer(Buffer waveformBuffer) {
        synthaxLFO.setBuffer(waveformBuffer);
    }

    /**
     * author Ellie Rosander
     * @param waveformBuffer
     */
    public void setViewLFOBuffer(Buffer waveformBuffer) {
        synthaxView.setKnobLFOWaveForm(waveformBuffer);

    }

    /**
     * Author Ellie Rosander
     * @return
     */
    public Boolean getLFOActive() {
        return synthaxLFO.getActive();
    }

    /**
     * author ellie Rosander
     * @return
     */
    public float getLFODepth() {
        return synthaxLFO.getDepthValue();
    }

    /**
     * Author Ellie Rosander
     * @return
     */
    public Buffer getLFOWaveForm() {
        return synthaxLFO.getWaveformBuffer();
    }

    /**
     * Author Ellie Rosander
     * @return
     */
    public float getLFOrate() {
        return synthaxLFO.getRateFrequency();
    }

    /**
     * Author Ellie Rosander
     * @return
     */
    public float getLFOPhase() {
        return synthaxLFO.getPhase();
    }

    /**
     * Author Ellie Rosander
     * @param phase
     */
    public void setLFOPhase(Float phase) {
        synthaxLFO.setPhase(phase);
    }


    /**
     * Author Oliver Berggren
     */
    //public void setDelayFeedback(float feedback) {
    //    synthaxDelay.setFeedbackDuration(feedback);
   // }

    //SynthaxDelay
    public float getDelayFeedback() {
        // Retrieve the feedback value from the model or UI component
        return synthaxDelay.getFeedbackDuration();
    }
    /**
     * Author Oliver Berggren
     */
    public float getDelayTime() {
        // Retrieve the time value from the model or UI component
        return synthaxDelay.getDelayTime();
    }
    /**
     * Author Oliver Berggren
     */
    public float getDelayDecay() {
        // Retrieve the decay value from the model or UI component
        return synthaxDelay.getDecay();
    }
    /**
     * Author Oliver Berggren
     */
    public float getDelayLevel() {
        // Retrieve the level value from the model or UI component
        return synthaxDelay.getLevel();
    }



    //VIEW
    public float getViewDelayFeedback() {
        return synthaxView.getKnobDelayFeedback();
    }
    public float getViewDelayTime() {
        return synthaxView.getKnobDelayTime();
    }

    public float getViewDelayDecay() {
        return synthaxView.getKnobDelayDecay();
    }

    public float getViewDelayLevel() {
        return synthaxView.getKnobDelayLevel();
    }

    public void setViewDelayFeedback(float feedback) {
        synthaxView.setKnobDelayFeedback(feedback);
    }
    public void setViewDelayTime(float feedback) {
        synthaxView.setKnobDelayTime(feedback);
    }
    public void setViewDelayDecay(float feedback) {
        synthaxView.setKnobDelayDecay(feedback);
    }
    public void setViewDelayLevel(float feedback) {
        synthaxView.setKnobDelayLevel(feedback);
    }
}
