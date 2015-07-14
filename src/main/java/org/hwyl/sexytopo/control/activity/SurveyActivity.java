package org.hwyl.sexytopo.control.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.hwyl.sexytopo.R;
import org.hwyl.sexytopo.control.Log;
import org.hwyl.sexytopo.control.SurveyManager;
import org.hwyl.sexytopo.control.io.Saver;
import org.hwyl.sexytopo.control.util.SurveyStats;
import org.hwyl.sexytopo.control.util.TextTools;
import org.hwyl.sexytopo.model.survey.Survey;
import org.hwyl.sexytopo.test.TestSurveyCreator;

public class SurveyActivity extends SexyTopoActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        Survey survey = SurveyManager.getInstance(this).getCurrentSurvey();
        TextView nameField = (TextView)(findViewById(R.id.survey_name));
        nameField.setText(survey.getName());

        View saveButton = findViewById(R.id.buttonSaveSurvey);
        saveButton.setOnClickListener(this);
        View generateButton = findViewById(R.id.buttonGenerateSurvey);
        generateButton.setOnClickListener(this);
    }

    @Override
    protected void onResume() {

        super.onResume();
        updateStats();



    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.buttonGenerateSurvey:
                Survey currentSurvey = TestSurveyCreator.create(10, 5);
                SurveyManager.getInstance(this).setCurrentSurvey(currentSurvey);
                break;
            case R.id.buttonSaveSurvey:
                try {
                    Saver.save(this, getSurvey());
                } catch (Exception e) {
                    showSimpleToast("Error saving survey");
                    Log.e("Error saving survey: " + e);
                }
        }
    }

    private void updateStats() {

        Survey survey = getSurvey();
        double length = SurveyStats.calcTotalLength(survey);
        setStatsField(R.id.statsFieldLength, TextTools.formatTo2dp(length));
        double heightRange = SurveyStats.calcHeightRange(survey);
        setStatsField(R.id.statsFieldDepth, TextTools.formatTo2dp(heightRange));
        int numberOfStations = SurveyStats.calcNumberStations(survey);
        setStatsField(R.id.statsFieldNumberStations, TextTools.format(numberOfStations));
        double shortestLeg = SurveyStats.calcShortestLeg(survey);
        setStatsField(R.id.statsFieldShortestLeg, TextTools.formatTo2dp(shortestLeg));
        double longestLeg = SurveyStats.calcLongestLeg(survey);
        setStatsField(R.id.statsFieldLongestLeg, TextTools.formatTo2dp(longestLeg));

    }

    private void setStatsField(int id, String text) {
        TextView textView = (TextView)findViewById(id);
        textView.setText(text);
    }

}
