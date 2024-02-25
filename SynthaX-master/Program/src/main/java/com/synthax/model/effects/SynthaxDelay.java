package com.synthax.model.effects;

import com.synthax.util.HelperMath;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.*;

/**
 * Class that handles the delay effect
 * @author Teodor Wegestål
 * @author Viktor Lenberg
 */
public class SynthaxDelay {
    private final float maxDelayTime = 3000.0f;
    private float feedbackDuration = 100.0f;

    private float cachedLevelValue;
    private float cachedDelayTime;
    private float cachedDecayValue;
    private float cachedFeedbackDuration;
    private boolean isActive = false;

    private TapIn delayIn;
    private TapOut delayOut;
    private Glide decayGlide;
    private Envelope delayFeedbackEnvelope;
    private Glide levelGlide;
    private Gain output;

    public SynthaxDelay(UGen filterOutput) {
        Gain synthGain = new Gain(AudioContext.getDefaultContext(), 1, 1.0f);
        synthGain.addInput(filterOutput);

        delayIn = new TapIn(AudioContext.getDefaultContext(), maxDelayTime);
        delayIn.addInput(synthGain);

        //this object controls the time of the delay
        delayOut = new TapOut(AudioContext.getDefaultContext(), delayIn, 100.0f);

        //this Gain object controls the decay of the delay
        decayGlide = new Glide(AudioContext.getDefaultContext(), 0.0f);
        Gain delayGain = new Gain(AudioContext.getDefaultContext(), 1, decayGlide);
        delayGain.addInput(delayOut);

        //this Envelope object controls the feedback duration of the delay
        delayFeedbackEnvelope = new Envelope(AudioContext.getDefaultContext(), 0.0f);
        Gain feedBackGain = new Gain(AudioContext.getDefaultContext(), 1, delayFeedbackEnvelope);
        feedBackGain.addInput(delayGain);

        delayIn.addInput(feedBackGain);

        //this Gain object controls the level of the delay
        levelGlide = new Glide(AudioContext.getDefaultContext(), 0.0f, 20f);
        Gain finalDelayGain = new Gain(AudioContext.getDefaultContext(), 1, levelGlide);
        finalDelayGain.addInput(delayGain);

        output = new Gain(AudioContext.getDefaultContext(), 1, 1.0f);
        output.addInput(synthGain);
        output.addInput(finalDelayGain);
    }

    public Gain getOutput() {
        return output;
    }

    public void setActive() {
        isActive = !isActive;

        if(isActive) {
            feedbackDuration = cachedFeedbackDuration;
            delayOut.setDelay(cachedDelayTime);
            decayGlide.setValue(cachedDecayValue);
            levelGlide.setValue(cachedLevelValue);

        } else {
            cachedFeedbackDuration = feedbackDuration;
            cachedDelayTime = delayOut.getDelay();
            cachedDecayValue = decayGlide.getValue();
            cachedLevelValue = levelGlide.getValue();

            delayOut.setDelay(0.0f);
            decayGlide.setValue(0.0f);
            levelGlide.setValue(0.0f);
            feedbackDuration = 0.0f;
        }
    }

    public void setFeedbackDuration(float feedbackDuration) { //TODO dubbelkolla denna
        System.out.println("setFeedbackDuration(float feedbackDuration) feedbackDuration " + feedbackDuration + "& cachedFeedbackDuration " + cachedFeedbackDuration);
        cachedFeedbackDuration = feedbackDuration;
    }
    public void setDelayTime(float delayTime) {
        cachedDelayTime = delayTime;
    }

    public void setDecay(float decayValue) {
        cachedDecayValue = decayValue;

    }

    public void setLevel(float levelValue) {
        cachedLevelValue = levelValue;
    }


    public Envelope getEnvelope() {
        return delayFeedbackEnvelope;
    }

    public float getFeedbackDuration() { //check if correct
        // Check if the delay effect is active
            // Return the actual feedback duration if the effect is active
            System.out.println("SyntaxDelay: getFeedbackDuration() == "+ cachedFeedbackDuration + " (active)");
            return cachedFeedbackDuration;
       /** } else {
            // Return a default or zero value if the effect is not active
            System.out.println("1 SyntaxDelay: getFeedbackDuration() == 0.0 (not active)");
            System.out.println("2 SyntaxDelay: getFeedbackDuration() cachedFeedbackDuration:" + cachedFeedbackDuration +" (not active");
            System.out.println("2 SyntaxDelay: getFeedbackDuration() feedbackDuration:" + feedbackDuration + " (not active");
            System.out.println();
            return feedbackDuration;
        }*/
    }

    public float getDelayTime() {
        // Check if the delay effect is active
        if (isActive) {
            //considering whether the effect is active
            //return delayOut.getValue();
            System.out.println("SyntaxDelay: getDelayTime() == " + "delayOut.getDelay(): " + delayOut.getDelay()+ " (active)");
            return HelperMath.map(delayOut.getDelay(), 100, 1000, 0, 1);
        } else {
            System.out.println("SyntaxDelay: cachedDelayTime: " + cachedDelayTime + " (not active)");
            return cachedDelayTime;
        }
    }

    public float getDecay() {
        // Check if the delay effect is active
        if (isActive) {
            //considering whether the effect is active
            System.out.println("SyntaxDelay: getDecay() == " + "decayGlide.getValue(): " + decayGlide.getValue() + " (active)");
            return decayGlide.getValue();
        } else {
            System.out.println("SyntaxDelay: "+"cachedDecayValue: " + cachedDecayValue + " (not active)");
            return cachedDecayValue;
        }
    }
    public float getLevel() {
        // Check if the delay effect is active
        if (isActive) {
            //considering whether the effect is active
            System.out.println("SyntaxDelay: getLevel() == " + "levelGlide.getValue(): " + levelGlide.getValue() + " (active)");
            return levelGlide.getValue();
        } else {
            System.out.println("SyntaxDelay: cachedLevelValue: " + cachedLevelValue + " (not active)");
            return cachedLevelValue;
        }
    }
}
