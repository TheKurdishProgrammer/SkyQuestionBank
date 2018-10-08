package com.example.mohammed.skyquestionbank.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.mohammed.skyquestionbank.R;
import com.example.mohammed.skyquestionbank.adapters.CategoryAdapter;
import com.example.mohammed.skyquestionbank.databinding.FragmentChooseCriteriaBinding;
import com.example.mohammed.skyquestionbank.firebase.FirebaseQuestionReferences;
import com.example.mohammed.skyquestionbank.interfaces.OnRecyclerItemClick;
import com.example.mohammed.skyquestionbank.interfaces.OnResponseCallback;
import com.example.mohammed.skyquestionbank.models.Category;
import com.example.mohammed.skyquestionbank.models.CategoryResponse;
import com.example.mohammed.skyquestionbank.networking.QuestionDataDownloader;
import com.example.mohammed.skyquestionbank.utils.InternetConnectivityChecker;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

import static com.example.mohammed.skyquestionbank.interfaces.FirebaseRefLinks.DUEL_CHALLENGE_STATUS;
import static com.example.mohammed.skyquestionbank.ui.DuelChallengeActivity.DUEL_MATCH_READY;
import static com.example.mohammed.skyquestionbank.ui.MainActivity.UID;
import static com.example.mohammed.skyquestionbank.ui.QuestionActivity.AMOUNT;
import static com.example.mohammed.skyquestionbank.ui.QuestionActivity.CAT_ID;
import static com.example.mohammed.skyquestionbank.ui.QuestionActivity.DIFFICULTY;
import static com.example.mohammed.skyquestionbank.ui.QuestionActivity.GAME_STATUS_KEY;
import static com.example.mohammed.skyquestionbank.ui.QuestionActivity.PLAYER_TYPE;
import static com.example.mohammed.skyquestionbank.ui.QuestionActivity.STATUS_MULTIPLE;
import static com.example.mohammed.skyquestionbank.ui.QuestionActivity.TYPE;
import static com.example.mohammed.skyquestionbank.ui.QuestionActivity.TYPE_OPPONENT;


public class ChooseQuestionCriteriaFragment extends Fragment implements OnResponseCallback<CategoryResponse>, OnRecyclerItemClick {

    public static final String SELECTED_CATEGORY = "SELECTED_CATEGORY";
    private static final String CATEGORY_ID = "CATEGORY_ID";
    private static final String SCROLL_POSITION = "SCROLL_POSITION";
    private static final String CATEGORY_RESPONSE = "CATEGORY_RESPONSE";
    private List<Category> categories;
    private int lastClickedCategoryRadionPos = -1;
    private FragmentChooseCriteriaBinding binding;


    public final static String DIFFICULTY_EASY = "easy";
    public final static String DIFFICULTY_MEDIUM = "medium";
    public final static String DIFFICULTY_HARD = "hard";


    public final static String TYPE_boolean = "boolean";
    public final static String TYPE_multiple = "multiple";

    private int[] scrollPosition;
    private int categoryId;
    private int categoryPosition;
    private CategoryResponse response;

    public ChooseQuestionCriteriaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_choose_criteria, container, false);
        setListeners();

        if (savedInstanceState != null) {
            categoryPosition = savedInstanceState.getInt(SELECTED_CATEGORY);
            categoryId = savedInstanceState.getInt(CATEGORY_ID);
            scrollPosition = savedInstanceState.getIntArray(SCROLL_POSITION);
            response = savedInstanceState.getParcelable(CATEGORY_RESPONSE);
            populateUi(response);
        } else {

            getCategories();
        }
        return binding.getRoot();
    }

    private void getCategories() {
        QuestionDataDownloader dataDownloader = new QuestionDataDownloader();
        dataDownloader.getCategories(this);
    }


    @Override
    public void onStart() {
        super.onStart();
        if (response == null)
            getCategories();
        else
            populateUi(response);
    }

    private void setListeners() {
        binding.startChallenge.setOnClickListener(v -> {

            if (response == null) {
                if (InternetConnectivityChecker.isConnected(getContext()))
                    getCategories();
                else
                    Snackbar.make(binding.startChallenge, R.string.no_connection, Snackbar.LENGTH_LONG).show();

                return;
            }
            Intent intent = getIntentWithCriteria(new Intent(getContext(), QuestionActivity.class));

            Bundle bundle = getArguments();

            if (bundle != null) {
                String uid = bundle.getString(UID);
                intent.putExtra(UID, uid);
                intent.putExtra(GAME_STATUS_KEY, STATUS_MULTIPLE);
                intent.putExtra(PLAYER_TYPE, TYPE_OPPONENT);
                DatabaseReference meAsOpponentRef = FirebaseQuestionReferences.getMeAsOpponentRef(uid);
                meAsOpponentRef.child(DUEL_CHALLENGE_STATUS).setValue(DUEL_MATCH_READY);
            }

            startActivity(intent);
        });

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (view == null)
                    return;

                parent.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                TextView amount = (TextView) parent.getChildAt(0);
                amount.setTextColor(ContextCompat.getColor(getContext(), R.color.md_white_1000));
                amount.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(SELECTED_CATEGORY, categoryPosition);
        outState.putInt(CATEGORY_ID, categoryId);
        outState.putParcelable(CATEGORY_RESPONSE, response);

        outState.putIntArray(SCROLL_POSITION, new int[]{binding.criteriaScroll.getScrollX(), binding.criteriaScroll.getScrollY()});
        super.onSaveInstanceState(outState);
    }

    private Intent getIntentWithCriteria(Intent intent) {

        int typeId = binding.type.getCheckedRadioButtonId();
        String type = null;


        switch (typeId) {
            case R.id.bool:
                type = TYPE_boolean;
                break;
            case R.id.multi:
                type = TYPE_multiple;
                break;
        }


        int difficultyId = binding.difficulity.getCheckedRadioButtonId();

        String difficulty = null;

        switch (difficultyId) {
            case R.id.easy:
                difficulty = DIFFICULTY_EASY;
                break;
            case R.id.medium:
                difficulty = DIFFICULTY_MEDIUM;
                break;
            case R.id.hard:
                difficulty = DIFFICULTY_HARD;
                break;

        }

        int amount = Integer.parseInt(binding.spinner.getSelectedItem().toString());


        intent.putExtra(AMOUNT, amount);
        intent.putExtra(DIFFICULTY, difficulty);
        intent.putExtra(TYPE, type);
        intent.putExtra(CAT_ID, categoryId);

        Log.e("CATEGORY_ID", String.valueOf(categoryId));

        return intent;
    }


    @Override
    public void onResponse(CategoryResponse response) {

        this.response = response;
        populateUi(response);
    }

    private void populateUi(CategoryResponse response) {

        CategoryAdapter adapter = new CategoryAdapter(categoryPosition, getContext(), response.getTriviaCategories(), this);
        binding.questionCategory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.questionCategory.setHasFixedSize(true);
        binding.questionCategory.setAdapter(adapter);
        categoryId = response.getTriviaCategories().get(0).getId();
        categories = response.getTriviaCategories();
        binding.waitDialogLayout.waitDialog.setVisibility(View.GONE);

        if (scrollPosition != null)
            binding.criteriaScroll.post(() -> binding.criteriaScroll.scrollTo(scrollPosition[0], scrollPosition[1]));

    }

    @Override
    public void onError(Throwable throwable) {
        Log.e(getClass().getSimpleName(), throwable.getMessage());
        binding.waitDialogLayout.waitDialog.setVisibility(View.GONE);
        Snackbar.make(binding.startChallenge, R.string.no_connection, Snackbar.LENGTH_LONG).show();
    }

    public void onItemClicked(int position) {

        categoryPosition = position;
        categoryId = categories.get(position).getId();
    }

    public void onItemClicked(int position, String s) {

    }

}
