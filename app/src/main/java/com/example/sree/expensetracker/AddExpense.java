package com.example.sree.expensetracker;


import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddExpense extends Fragment
{
    Button add;
    TextView message;
    EditText description;
    EditText amount;
    DatePicker date;
    View inflatedView = null;
    private  Database database;
    private SQLiteDatabase sqLiteDatabaseObj;

    public AddExpense() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_add_expense, container, false);

        description = (EditText) inflatedView.findViewById(R.id.descriptionText);
        date = (DatePicker) inflatedView.findViewById(R.id.dateText);
        amount = (EditText) inflatedView.findViewById(R.id.amountText);
        message = (TextView) inflatedView.findViewById(R.id.message);
        add = (Button) inflatedView.findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String desc = description.getText().toString();
                if(desc.equals(""))
                {
                    message.setTextColor(Color.rgb(255,0,0));
                    message.setText("Add Description");
                    description.requestFocus();
                    return;
                }
                else if(amount.getText().toString().equals(""))
                {
                    message.setTextColor(Color.rgb(255,0,0));
                    message.setText("Add Amount");
                    amount.requestFocus();
                    return;
                }
                Float amt = Float.parseFloat(amount.getText().toString());
                int day = date.getDayOfMonth();
                int month = date.getMonth()+1;
                int year = date.getYear();

                SharedPreferences sharedPref = getActivity().getSharedPreferences("emailPref", 0);
                String savedEmailId = sharedPref.getString("enteredEmailId", "");

                String date = year+"-"+month+"-"+day;
                database =  (new Database(getContext()));
                sqLiteDatabaseObj = database.getWritableDatabase();
                boolean check = database.insertExpense(sqLiteDatabaseObj,savedEmailId,desc,amt,date);
                if(check)
                {
                    message.setTextColor(Color.rgb(0,255,0));
                    message.setText("Expense Added!!");
                    description.setText("");

                    amount.setText("");
                    description.requestFocus();
                }
                else
                {
                    message.setTextColor(Color.rgb(255,0,0));
                    message.setText("Error!! Expense not added.");
                }
            }
        });

        return inflatedView;
    }

}
