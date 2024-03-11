package com.synthax.view;

import com.synthax.controller.VoiceController;
import com.synthax.view.utils.Dialogs;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.controlsfx.control.ToggleSwitch;
import java.util.prefs.Preferences;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;


public class SettingsView implements Initializable {
    @FXML private ToggleSwitch toggleMonophonic;
    @FXML private VBox presetsList;
    @FXML private Spinner<Integer> voiceCountSpinner;
    @FXML private ComboBox<String> cmbLoadPresets;
    @FXML private VBox programPresetList;
    private SynthaxView synthaxView;
    private String selectedPresetName = "";
    private static final String PREF_SELECTED_PRESET_NAME = "selectedPresetName";


    // TODO: 2022-05-20 Only pass call to controller,
    //  then let controller create thread and call on SeqPresetLoader
    //  and when that thread is done - callBack to update GUI.
    @FXML
    public void onActionDelete() {
        int choice = Dialogs.getConfirmationYesCancel("Remove Preset", "This will remove the selected presets, are you sure?");

        if (choice == Dialogs.YES_OPTION) {
            for (int i = 0; i < presetsList.getChildren().size(); i++) {
                CheckBox c = (CheckBox) presetsList.getChildren().get(i);
                if (c.isSelected()) {
                    presetsList.getChildren().remove(i);
                    synthaxView.deletePreset(c.getText());
                    i--;
                }
            }
            synthaxView.updateSequencerPresetList();
        }
    }

    /**
     * @author Ellie Rosander
     * Används för att uppdatera och sätta actionEvent på cmbLoadPresets,
     * för att ladda in program presets
     * cmbLoadPresets är knapp tillagd av mig i
     * Settings-view.fxml
     */

    private void initProgramPresetButtons() {
        synthaxView.updateProgramPresetList();
        cmbLoadPresets.setOnAction(actionEvent -> {
            System.out.println(cmbLoadPresets.getValue());
            selectedPresetName = cmbLoadPresets.getValue();
            saveSelectedPresetName(selectedPresetName);
            synthaxView.onSelectProgramPreset(cmbLoadPresets.getValue());
        });




    }

    private void setMonophonicState(boolean monophonic) {
        if(monophonic) {
            synthaxView.setOscMonophonic();
        } else {
            synthaxView.setOscVoiceCount(voiceCountSpinner.getValue());
        }
    }

    public void populatePresetsBox(String[] presetName, SynthaxView synthaxView) {
        this.synthaxView = synthaxView;
        initProgramPresetButtons();
        for (String s : presetName) {
            presetsList.getChildren().add(new CheckBox(s));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadSelectedPresetName();

        if (!selectedPresetName.isEmpty()) {
            cmbLoadPresets.getSelectionModel().select(selectedPresetName);
        }

        toggleMonophonic.setSelected(VoiceController.MONOPHONIC_STATUS);
        toggleMonophonic.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
            setMonophonicState(newValue);
        });

        SpinnerValueFactory<Integer> svf = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, VoiceController.MAX_OSC_VOICE_COUNT, VoiceController.VOICE_COUNT);
        voiceCountSpinner.setValueFactory(svf);
        voiceCountSpinner.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            synthaxView.setOscVoiceCount(newValue);
            if(toggleMonophonic.isSelected()) {
                toggleMonophonic.setSelected(false);
            }
        });

    }

    /**
     * @author Ellie Rosander
     */
    @FXML
    public void onActionSavePresetTest() {

        savePresetDialog();
        initProgramPresetButtons();
    }

    /**
     * @author Ellie Rosander
     * dialog som säger att komponenter måste ha  boolean isActive för att kunna sparas
     * Samt tar namn på preset som ska sparas
     */
    public void savePresetDialog() {
        int answer = Dialogs.getConfirmationYesNoCancel("Information", "Only components that are set to active can ba saved");
        if(answer == 1) {
            String presetName = Dialogs.getTextInput("Preset Name", "Preset Name:", "currentPresetName");
            if(presetName != null && !presetName.equals("")) {
                synthaxView.onActionSavePresetTest(presetName);
            }

        }

    }

    /**
     * @author Ellie Rosander, Edin Jahic
     * metod för att uppdatera cmbLoadPresets med presets från resources>program_presets
     * @param presetNames
     * @param chosenPreset
     */
    public void setProgramPresetList(String[] presetNames, String chosenPreset) {
        Platform.runLater(() -> {
            cmbLoadPresets.setItems(FXCollections.observableList(Arrays.asList(presetNames)));
            if (selectedPresetName.isEmpty()) {
                cmbLoadPresets.setPromptText("Default");
            } else {
                cmbLoadPresets.getSelectionModel().select(selectedPresetName);
            }
        });
    }

    /**
     * @author Edin Jahic
     * Metod för att fylla listan med de presets som finns sparade i programmet.
     * @param programPresetList
     */
    public void populateProgramPresets(String[] programPresetList) {
        initProgramPresetButtons();
        for (String presetName : programPresetList) {
            this.programPresetList.getChildren().add(new CheckBox(presetName));
        }
    }

    /**
     * @author Edin Jahic
     * Metod som hanterar vad som händer när man klickar på delete ikonen i remove presets panelen.
     */
    public void onActionDeleteProgramPresets() {
        int choice = Dialogs.getConfirmationYesCancel("Remove Program Preset", "This will remove the selected program presets, are you sure?");

        if (choice == Dialogs.YES_OPTION) {
            for (int i = 0; i < programPresetList.getChildren().size(); i++) {
                CheckBox c = (CheckBox) programPresetList.getChildren().get(i);
                if (c.isSelected()) {
                    programPresetList.getChildren().remove(i);
                    synthaxView.deleteProgramPreset(c.getText());
                    i--;
                }
            }
            synthaxView.updateProgramPresetList();
        }
    }

    /**
     * @author Edin Jahic
     * Används för att ladda in senast valda presets och visa dessa i cmbLoadPresets.
     */
    private void loadSelectedPresetName() {
        Preferences prefs = Preferences.userNodeForPackage(SettingsView.class);
        selectedPresetName = prefs.get(PREF_SELECTED_PRESET_NAME, "");
    }

    /**
     * @author Edin Jahic
     * Används för att spara senast valda presets för att sedan kunna ladda in dessa även efter man stängt ner och
     * öppnar settings på nytt.
     */
    private void saveSelectedPresetName(String presetName) {
        Preferences prefs = Preferences.userNodeForPackage(SettingsView.class);
        prefs.put(PREF_SELECTED_PRESET_NAME, presetName);
    }
}
