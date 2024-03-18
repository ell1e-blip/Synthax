package com.synthax.model.effects;

import com.synthax.controller.OscillatorController;
import com.synthax.controller.VoiceController;
import com.synthax.model.enums.Waveforms;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.Gain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for requirement [FK_A_EFF_001].
 * @author Edin Jahic
 */
public class ReverbUnitTest {
    private SynthaxReverb synthaxReverb;

    @BeforeEach
    public void initialize() {
        UGen dummyUgen = new Gain(AudioContext.getDefaultContext(), 1, 0);
        synthaxReverb = new SynthaxReverb(dummyUgen);
    }

    /**
     * Tests the requirement [FK_S_EFF_001.1].
     *
     * [FK_S_EFF_001.1]: Ändra reverbets rumstorlek:
     * Rumstorleken på reverbet ska kunna justeras.
     *
     * @throws Exception
     */
    @Test
    public void testReverbSize() throws Exception {
        synthaxReverb.setReverbSize(0.5f);
        assertEquals(0.5f, synthaxReverb.getReverbSize(), 0.001f);
    }

    /**
     * Tests the requirement [FK_A_EFF_001.2].
     *
     * [FK_S_EFF_001.2] Ändra reverbets ton:
     * Tonen på reverbet ska kunna justeras.
     *
     * @throws Exception
     */
    @Test
    public void testReverbTone() throws Exception {
        synthaxReverb.setReverbTone(0.8f);
        assertEquals(0.8f, synthaxReverb.getReverbTone(), 0.001f);
    }

    /**
     * Tests the requirement [FK_A_EFF_001.3].
     *
     * [FK_S_EFF_001.3] Ändra reverbets mängd:
     * Mängden reverb som ska appliceras på ljudet ska kunna justeras.
     *
     * @throws Exception
     */
    @Test
    public void testReverbAmount() throws Exception {
        synthaxReverb.setReverbAmount(0.3f);
        assertEquals(0.3f, synthaxReverb.getReverbAmount(), 0.001f);
    }
}

