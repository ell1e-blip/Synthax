package com.synthax.model.oscillator;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Function;
import net.beadsproject.beads.ugens.WavePlayer;

/**
 * Class controlling the low frequency oscillator which modulates a frequency of an oscillator.
 * @author Teodor Wegestål
 * @author Viktor Lenberg
 */
public class OscillatorLFO {
    private WavePlayer lfo;
    private float depth = 0f;

    private float rate = 0f;
    private float playedFrequency = 0f;
    private Function frequencyModulation;

    public OscillatorLFO() {
        lfo = new WavePlayer(AudioContext.getDefaultContext(), 0.1f, Buffer.SINE);

         // This function recalculates the sound buffer of the LFO representing its wave.
        frequencyModulation = new Function(lfo) {
            @Override
            public float calculate() {
                return (x[0] * depth) + playedFrequency;
            }
        };
    }

    public Function getFrequencyModulation() {
        return frequencyModulation;
    }

    public float getPlayedFrequency() {
        return playedFrequency;
    }

    public void setPlayedFrequency(float playedFrequency) {
        this.playedFrequency = playedFrequency;
    }

    public void setRate(float rate) {
        rate = convertRate(rate);
        this.rate = rate;
        lfo.setFrequency(rate);
    }

    public void setDepth(float depth) {
        this.depth = depth * 35;
    }


    public float convertRate(float rate) {
        //Converts the passed float value to correspond to the range used by the LFO
        return (float) (rate * 19.9 + 0.1);
    }

    /**
     * @author Edin Jahic
     * @param rate
     * @return
     */
    private float deConvertRate(float rate) {
        return (rate - 0.1f)/19.9f;
    }

    /**
     * @author Edin Jahic
     * @return
     */
    public float getDepth() {
        return depth;
    }

    /**
     * @author Edin Jahic
     * @return
     */
    public float getRate() {
        return deConvertRate(lfo.getFrequency());
    }
}
