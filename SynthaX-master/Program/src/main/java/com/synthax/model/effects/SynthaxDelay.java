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

    public boolean getActive() {
        return this.isActive;
    }
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
    public void setActive() {
        System.out.println("active method");
        this.isActive = !isActive;
        setIsActive(isActive);

        System.out.println("After: " + getActive());
        if (isActive) {
            this.delayOut.setDelay(cachedDelayTime);
            this.decayGlide.setValue(cachedDecayValue);
            this.levelGlide.setValue(cachedLevelValue);
            this.delayFeedbackEnvelope.setValue(cachedFeedbackDuration);

        } else {
            this.cachedDelayTime = this.delayOut.getDelay();
            this.cachedDecayValue = this.decayGlide.getValue();
            this.cachedLevelValue = this.levelGlide.getValue();
            this.cachedFeedbackDuration = this.delayFeedbackEnvelope.getValue();

            this.delayFeedbackEnvelope.setValue(0.0f); //TODO colla att de stämmer
            this.delayOut.setDelay(0.0f);
            this.decayGlide.setValue(0.0f);
            this.levelGlide.setValue(0.0f);
        }
    }


    // New method to update delay parameters without toggling isActive
    public void updateDelayParameters(float feedbackDuration, float delayTime, float decayValue, float levelValue) {
        this.feedbackDuration = feedbackDuration;
        this.delayOut.setDelay(delayTime);
        this.decayGlide.setValue(decayValue);
        this.levelGlide.setValue(levelValue);
    }

    public void setFeedbackDuration(float feedbackDuration) { //TODO dubbelkolla denna
        //System.out.println("setFeedbackDuration(float feedbackDuration) feedbackDuration " + feedbackDuration + "& cachedFeedbackDuration " + cachedFeedbackDuration);
        if (feedbackDuration < 0 || feedbackDuration > 5000) { // Assuming 5000ms is the max acceptable feedback duration
            throw new IllegalArgumentException("Feedback duration must be between 0 and 5000 milliseconds.");
        }
        if (getActive()) {
            // If delay effect is active, directly set the value
            this.feedbackDuration = feedbackDuration;
            this.delayFeedbackEnvelope.setValue(feedbackDuration);
            System.out.println("feedbackDuration setvalue "+feedbackDuration);
            System.out.println("feedbackDuration getvalue "+getFeedbackDuration());
        } else {
            // If not active, just cache the value
            this.cachedFeedbackDuration = feedbackDuration;
        }
    }
    public void setDelayTime(float delayTime) {
        if (isActive) {
            // If delay effect is active, directly set the value
            this.delayOut.setDelay(delayTime);
            System.out.println("delayTime setvalue "+delayTime);
            System.out.println("delayTime getvalue "+getDelayTime());
        } else {
            // If not active, just cache the value
            this.cachedDelayTime = delayTime;
        }
    }

    public void setDecay(float decayValue) {
        System.out.println("getActive() "+ getActive());
        if (isActive) {
            // If delay effect is active, directly set the value
            this.decayGlide.setValue(decayValue);
            System.out.println("decayValue setvalue "+decayValue);
            System.out.println("decayValue getvalue "+getDecay());
        } else {
            // If not active, just cache the value
            this.cachedDecayValue = decayValue;
            System.out.println("hello 2");
        }
    }

    public void setLevel(float levelValue) {
        if (isActive) {
            // If delay effect is active, directly set the value
            this.levelGlide.setValue(levelValue);
            System.out.println("levelValue setvalue" + levelValue);
            System.out.println("levelValue getvalue "+ getLevel());
        } else {
            // If not active, just cache the value
            this.cachedLevelValue = levelValue;
        }
    }
    public float getFeedbackDuration() {
        if (isActive) {
            // If the delay effect is currently active
            return this.feedbackDuration; // Return the current feedback duration value
        } else {
            // If the delay effect is not active
            return this.cachedFeedbackDuration; // Return the cached feedback duration value
        }
    }

    public float getDelayTime() {
        System.out.println("delay get getActive() "+ getActive());
        if (isActive) {
            // If the delay effect is currently active
            //System.out.println("delayOut.getDelay(): " +delayOut.getDelay());
            return this.delayOut.getDelay(); // Return the current delay time from delayOut
        } else {
            // If the delay effect is not active
           return this.cachedDelayTime; // Return the cached delay time value
        }
    }

    public float getDecay() {
        if (isActive) {
            // If the delay effect is currently active
            //System.out.println("decayGlide.getValue() " +decayGlide.getValue());
            return this.decayGlide.getValue(); // Return the current decay value from decayGlide
        } else {
            // If the delay effect is not active
            return this.cachedDecayValue; // Return the cached decay value
        }
    }

    public float getLevel() {
        if (isActive) {
            // If the delay effect is currently active
           // System.out.println("levelGlide.getValue() " +levelGlide.getValue());
            return this.levelGlide.getValue(); // Return the current level value from levelGlide
        } else {
            // If the delay effect is not active
            return this.cachedLevelValue; // Return the cached level value
        }
    }


    public Envelope getEnvelope() {
        return this.delayFeedbackEnvelope;
    }
    /**
    // Getters for current active parameters
    public float getFeedbackDuration() {
        return feedbackDuration;
    }

    public float getDelayTime() {
        return delayOut.getDelay();
    }

    public float getDecay() {
        return decayGlide.getValue();
    }

    public float getLevel() {
        return levelGlide.getValue();
    }*/
/**
    public float getFeedbackDuration() { //check if correct
        // Check if the delay effect is active
        if (isActive) {
            // Return the actual feedback duration if the effect is active
            System.out.println("SyntaxDelay: getFeedbackDuration() == "+ cachedFeedbackDuration + " (active)");
            return cachedFeedbackDuration;
        } else{
            // Return a default or zero value if the effect is not active
            System.out.println("1 SyntaxDelay: getFeedbackDuration() == 0.0 (not active)");
            System.out.println("2 SyntaxDelay: getFeedbackDuration() cachedFeedbackDuration:" + this.cachedFeedbackDuration + " (not active");
            System.out.println("2 SyntaxDelay: getFeedbackDuration() feedbackDuration:" + this.feedbackDuration + " (not active");
            System.out.println();
            return feedbackDuration;
        }
    }

    public float getDelayTime() {
        // Check if the delay effect is active
        if (isActive) {
            //considering whether the effect is active
            //return delayOut.getValue();
            System.out.println("SyntaxDelay: getDelayTime() == " + "delayOut.getValue(): " + cachedDelayTime + " (active)");
            return cachedDelayTime;
        } else {
            System.out.println("SyntaxDelay: cachedDelayTime: " + delayOut.getValue() + " (not active)");
            return delayOut.getValue();
        }
    }

    public float getDecay() {
        // Check if the delay effect is active
        if (isActive) {
            //considering whether the effect is active
            System.out.println("SyntaxDelay: getDecay() == " + ": " + cachedDecayValue + " (active)");
            return cachedDecayValue;
        } else {
            System.out.println("SyntaxDelay: " + decayGlide.getValue() + " (not active)");
            return decayGlide.getValue();
        }
    }
    public float getLevel() {
        // Check if the delay effect is active
        if (isActive) {
            //considering whether the effect is active
            System.out.println("SyntaxDelay: getLevel() == " + "levelGlide.getValue(): " + cachedLevelValue + " (active)");
            return cachedLevelValue;
        } else {
            System.out.println("SyntaxDelay: " + levelGlide.getValue() + " (not active)");
            return levelGlide.getValue();
        }
    }*/


}
