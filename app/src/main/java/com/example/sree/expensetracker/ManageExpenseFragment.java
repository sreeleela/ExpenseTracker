package com.example.sree.expensetracker;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
public class ManageExpenseFragment extends Fragment {

    View inflatedView;
    EditText description;
    EditText amount;
    DatePicker date;
    Button edit;
    Button save;
    Button delete;
    TextView message;
    int itemId;
    String itemDescription;
    String itemAmount;
    String itemDate;

    public ManageExpenseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflatedView = inflater.inflate(R.layout.fragment_manage_expense, container, false);

        description = (EditText) inflatedView.findViewById(R.id.mdescriptionText);
        amount = (EditText) inflatedView.findViewById(R.id.mamountText);
        date = (DatePicker) inflatedView.findViewById(R.id.mdateText);
        edit = (Button) inflatedView.findViewById(R.id.medit);
        save = (Button) inflatedView.findViewById(R.id.msave);
        delete = (Button) inflatedView.findViewById(R.id.mdelete);
        message = (TextView) inflatedView.findViewById(R.id.mmessage);

        description.setEnabled(false);
        amount.setEnabled(false);
        date.setEnabled(false);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("emailPref", 0);
        final String savedEmailId = sharedPref.getString("enteredEmailId", "");
        description.setText(itemDescription);
        amount.setText(itemAmount);
        String dt[] = itemDate.split("-");
        date.updateDate(Integer.parseInt(dt[0]),Integer.parseInt(dt[1]),Integer.parseInt(dt[2]));
        save.setEnabled(false);

        edit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                description.setEnabled(true);
                amount.setEnabled(true);
                date.setEnabled(true);
                save.setEnabled(true);
                edit.setEnabled(false);
                description.requestFocus();
            }
        });
        delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
               Database db = new Database(getContext());
               db.deleteExpense(savedEmailId,itemId);

                FragmentManager fragments = getActivity().getSupportFragmentManager();
                fragments.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                FragmentTransaction fragmentTransaction = fragments.beginTransaction();
                ExpensesList expensesList = new ExpensesList();
                expensesList.setMessage("Expense deleted","success");
                fragmentTransaction.replace(R.id.fragment_container,expensesList);
                fragmentTransaction.commit();
            }
        });

        save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String localDescription = description.getText().toString();
                String localDate = date.getYear()+"-"+date.getMonth()+"-"+date.getDayOfMonth();

                if(localDescription.equals(""))
                {
                    message.setTextColor(Color.rgb(255,0,0));
                    message.setText("Enter Description.");
                    description.requestFocus();
                    return;
                }
                else if(amount.getText().toString().equals(""))
                {
                    message.setTextColor(Color.rgb(255,0,0));
                    message.setText("Enter Amount.");
                    amount.requestFocus();
                    return;
                }
                Float localAmount = Float.parseFloat(amount.getText().toString());
                Database db = new Database(getContext());
                boolean check = db.updateExpense(savedEmailId,itemId,localDescription,localAmount,localDate);
                if(check)
                {
                    FragmentManager fragments = getActivity().getSupportFragmentManager();
                    fragments.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    FragmentTransaction fragmentTransaction = fragments.beginTransaction();
                    ExpensesList expensesList = new ExpensesList();
                    expensesList.setMessage("Expense Updated","success");
                    fragmentTransaction.replace(R.id.fragment_container,expensesList);
                    fragmentTransaction.commit();
                }
                else
                {
                    FragmentManager fragments = getActivity().getSupportFragmentManager();
                    fragments.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    FragmentTransaction fragmentTransaction = fragments.beginTransaction();
                    ExpensesList expensesList = new ExpensesList();
                    expensesList.setMessage("Error occured","error");
                    fragmentTransaction.replace(R.id.fragment_container,expensesList);
                    fragmentTransaction.commit();
                }
            }
        });

        return inflatedView;
    }

    public void setItemData(String itemInformation)
    {
        String data = itemInformation;
        String splitedData[] = data.split("\n");
        itemId = Integer.parseInt(splitedData[0]);
        itemDescription = splitedData[2];
        itemAmount = splitedData[3].replace("$","");
        itemDate = splitedData[1];
    }

}
