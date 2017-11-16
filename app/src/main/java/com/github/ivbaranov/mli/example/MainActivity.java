package com.github.ivbaranov.mli.example;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.github.ivbaranov.mli.MaterialLetterIcon.Shape;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
  private static final String[] desuNoto = {
      "Alane Avey", "Belen Brewster", "Brandon Brochu", "Carli Carrol", "Della Delrio",
      "Esther Echavarria", "Etha Edinger", "Felipe Flecha", "Ilse Island", "Kecia Keltz",
      "Lourie Lucas", "Lucille Leachman", "Mandi Mcqueeney", "Murray Matchett", "Nadia Nero",
      "Nannie Nipp", "Ozella Otis", "Pauletta Poehler", "Roderick Rippy", "Sherril Sager",
      "Taneka Tenorio", "Treena Trentham", "Ulrike Uhlman", "Virgina Viau", " Willis Wysocki "
  };
  private static final String[] countries = {
      "Albania", "Australia", "Belgium", "Canada", "China", "Dominica", "Egypt", "Estonia",
      "Finland", "France", "Germany", "Honduras", "Italy", "Japan", "Madagascar", "Netherlands",
      "Norway", "Panama", "Portugal", "Romania", "Russia", "Slovakia", "Vatican", "Zimbabwe"
  };
  private static final int CONTACTS = 0;
  private static final int COUNTRIES = 1;
  private static final Random RANDOM = new Random();

  private RecyclerView recyclerView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
    setupRecyclerView();
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    final MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu, menu);

    return super.onCreateOptionsMenu(menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.contacts:
        setContactsAdapter(desuNoto);
        return true;
      case R.id.countries:
        setCountriesAdapter(countries);
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void setupRecyclerView() {
    recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
    setContactsAdapter(desuNoto);
  }

  private void setContactsAdapter(String[] array) {
    recyclerView.setAdapter(
        new SimpleStringRecyclerViewAdapter(this, Arrays.asList(array), CONTACTS));
  }

  private void setCountriesAdapter(String[] array) {
    recyclerView.setAdapter(
        new SimpleStringRecyclerViewAdapter(this, Arrays.asList(array), COUNTRIES));
  }

  public static class SimpleStringRecyclerViewAdapter
      extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private List<String> mValues;
    private int[] mMaterialColors;
    private int mType;

    public static class ViewHolder extends RecyclerView.ViewHolder {
      public String mBoundString;

      public final View mView;
      public final MaterialLetterIcon mIcon;
      public final TextView mTextView;

      public ViewHolder(View view) {
        super(view);
        mView = view;
        mIcon = (MaterialLetterIcon) view.findViewById(R.id.icon);
        mTextView = (TextView) view.findViewById(android.R.id.text1);
      }

      @Override public String toString() {
        return super.toString() + " '" + mTextView.getText();
      }
    }

    public String getValueAt(int position) {
      return mValues.get(position);
    }

    public SimpleStringRecyclerViewAdapter(Context context, List<String> items, int type) {
      context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
      mMaterialColors = context.getResources().getIntArray(R.array.colors);
      mBackground = mTypedValue.resourceId;
      mValues = items;
      mType = type;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view =
          LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
      view.setBackgroundResource(mBackground);
      return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(final ViewHolder holder, int position) {
      switch (mType) {
        case CONTACTS:
          holder.mIcon.setInitials(true);
          holder.mIcon.setInitialsNumber(2);
          holder.mIcon.setLetterSize(18);
          break;
        case COUNTRIES:
          holder.mIcon.setLettersNumber(3);
          holder.mIcon.setLetterSize(16);
          holder.mIcon.setShapeType(Shape.RECT);
          break;
      }
      holder.mBoundString = mValues.get(position);
      holder.mIcon.setShapeColor(mMaterialColors[RANDOM.nextInt(mMaterialColors.length)]);
      holder.mTextView.setText(mValues.get(position));
      holder.mIcon.setLetter(mValues.get(position));
    }

    @Override public int getItemCount() {
      return mValues.size();
    }
  }
}
