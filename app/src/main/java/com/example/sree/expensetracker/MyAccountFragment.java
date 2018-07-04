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
import android.widget.EditText;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyAccountFragment extends Fragment {
    View inflatedView;
    EditText firstName;
    EditText lastName;
    EditText email;
    EditText password;
    Button edit;
    Button save;
    Button cancel;
    TextView message;

    public MyAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        inflatedView  = inflater.inflate(R.layout.fragment_my_account, container, false);

        firstName = (EditText) inflatedView.findViewById(R.id.firstNameText);
        lastName = (EditText) inflatedView.findViewById(R.id.lastNameText);
        email = (EditText) inflatedView.findViewById(R.id.emailText);
        password = (EditText) inflatedView.findViewById(R.id.passwordText);
        edit = (Button) inflatedView.findViewById(R.id.edit);
        save = (Button) inflatedView.findViewById(R.id.save);
        cancel = (Button) inflatedView.findViewById(R.id.cancel);
        message = (TextView) inflatedView.findViewById(R.id.message);

        firstName.setEnabled(false);
        lastName.setEnabled(false);
        password.setEnabled(false);
        email.setEnabled(false);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("emailPref", 0);
        String savedEmailId = sharedPref.getString("enteredEmailId", "");
        String savedPassword = sharedPref.getString("enteredPassword", "");
        String savedFirstName = sharedPref.getString("firstName", "");
        String savedLastName = sharedPref.getString("lastName", "");

        email.setText(savedEmailId);
        firstName.setText(savedFirstName);
        lastName.setText(savedLastName);
        Database db = new Database(getContext());
        String loginData = db.getPassword(savedEmailId);
        String retrivedPassword[] = loginData.split(":");
        password.setText(retrivedPassword[1]);
        save.setEnabled(false);

        edit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                firstName.setEnabled(true);
                lastName.setEnabled(true);
                password.setEnabled(true);
                firstName.requestFocus();
                save.setEnabled(true);
                edit.setEnabled(false);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FragmentManager fragments = getActivity().getSupportFragmentManager();
                fragments.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                FragmentTransaction fragmentTransaction = fragments.beginTransaction();
                AddExpense addExpense = new AddExpense();
                fragmentTransaction.replace(R.id.fragment_container,addExpense);
                fragmentTransaction.commit();
            }
        });
        save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String localFirstName = firstName.getText().toString();
                String localLastName = lastName.getText().toString();
                String localPassword = password.getText().toString();
                int passwordLength = localPassword.length();

                if(localFirstName.equals(""))
                {
                    message.setTextColor(Color.rgb(255,0,0));
                    message.setText("Enter First Name.");
                    firstName.requestFocus();
                    return;
                }
                else if(localLastName.equals(""))
                {
                    message.setTextColor(Color.rgb(255,0,0));
                    message.setText("Enter Last Name.");
                    lastName.requestFocus();
                    return;
                }
                else if(localPassword.equals(""))
                {
                    message.setTextColor(Color.rgb(255,0,0));
                    message.setText("Enter Password.");
                    password.requestFocus();
                    return;
                }
                else if(passwordLength < 8)
                {
                    message.setTextColor(Color.rgb(255,0,0));
                    message.setText("Password too short.");
                    password.requestFocus();
                    return;
                }
                Database db = new Database(getContext());
                boolean check = db.updateAccount(localFirstName,localLastName,email.getText().toString(),localPassword);
                if(check)
                {
                    message.setTextColor(Color.rgb(0,255,0));
                    message.setText("Account Updated");
                    firstName.setEnabled(false);
                    lastName.setEnabled(false);
                    password.setEnabled(false);
                    save.setEnabled(false);
                    edit.setEnabled(true);

                    // Create object of SharedPreferences.
                    SharedPreferences sharedPref = getActivity().getSharedPreferences("emailPref",0);
                    //now get Editor
                    SharedPreferences.Editor editor = sharedPref.edit();
                    //put your value
                    editor.putString("enteredPassord", localPassword);
                    editor.putString("firstName", localFirstName);
                    editor.putString("lastName", localLastName);
                    editor.commit();
                }
                else
                {
                    message.setTextColor(Color.rgb(255,0,0));
                    message.setText("Error in updating aacount");
                }
            }
        });

        return inflatedView;
    }

}
