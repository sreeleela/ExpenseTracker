package com.example.sree.expensetracker;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;


public class ExpensesList extends ListFragment implements
        AdapterView.OnItemClickListener
{
    private CustomAdapter mAdapter;
    Map<String,Float> yearList = new ArrayMap<>();
    ArrayList<String> onlyYearList = new ArrayList<String>();
    ArrayList<String> onlyMonthList = new ArrayList<String>();
    Map<String,Float> monthList = new LinkedHashMap<>();
    ArrayList<Expense> expensesList;
    ArrayList<String> dateList;
    ArrayList<String> itemList =new ArrayList<String>();
    ArrayList<String> itemInfo =new ArrayList<String>();
    String months[] = {"January","February","March","April","May","June","July","August",
                        "September","October","November","December"};
    ManageExpense manageExpense;
    TextView message;
    View view;
    String msg;
    String typeMsg;

    public ExpensesList()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_item_list, container, false);

        message = (TextView) view.findViewById(R.id.listMessage);
        typeMsg="";
        if(typeMsg.equals("error"))
        {
            message.setTextColor(Color.rgb(255,0,0));
        }
        else
        {
            message.setTextColor(Color.rgb(0,255,0));
        }
        message.setText(msg);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        message = (TextView) view.findViewById(R.id.listMessage);
        DecimalFormat df = new DecimalFormat("###.##");

        SharedPreferences sharedPref = getActivity().getSharedPreferences("emailPref", 0);
        String savedEmailId = sharedPref.getString("enteredEmailId", "");
        Database d = new Database(getActivity());
        expensesList = d.getExpenses(savedEmailId);
        dateList = d.getDates(savedEmailId);

        for (int i = 0;i<expensesList.size();i++)
        {
            String dt[] = expensesList.get(i).getDate().split("-");
            if(yearList.containsKey(dt[0]))
            {
                String dtLocal[] = expensesList.get(i).getDate().split("-");
                float amt = yearList.get(dtLocal[0])+expensesList.get(i).getAmount();
                yearList.put(dtLocal[0],amt);
            }
            else
            {
                String dtlocal[] = expensesList.get(i).getDate().split("-");
                yearList.put(dtlocal[0],expensesList.get(i).getAmount());
                onlyYearList.add(dtlocal[0]);
            }
        }

        for(int i=0;i<onlyYearList.size();i++)
        {
            for (int j =0; j<expensesList.size();j++)
            {
                String dt[] = expensesList.get(j).getDate().split("-");
                if (onlyYearList.get(i).equals(dt[0]))
                {
                    if(monthList.containsKey(dt[1]))
                    {
                        float amt = monthList.get(dt[1])+expensesList.get(j).getAmount();
                        monthList.put(dt[1],amt);
                    }
                    else
                    {
                        monthList.put(dt[1],expensesList.get(j).getAmount());
                    }
                }
            }
            for (String key: monthList.keySet())
            {
                onlyMonthList.add(String.valueOf(monthList.get(key)));
            }
            monthList.clear();
        }

        if(expensesList.size()>0)
        {
            mAdapter = new CustomAdapter(getContext());
            String month = null;
            String year = null;
            int dateCount = 0;
            for (int i = 0; i < dateList.size(); i++)
            {
                String dts[] = dateList.get(i).split("-");
                if (i == 0)
                {
                    year = dts[0];
                    mAdapter.addSectionHeaderItem(year+"   $"+df.format(yearList.get(year)));
                    itemList.add(year);
                    itemInfo.add("year");
                }
                else if (!year.equals(dts[0]))
                {
                    year = dts[0];
                    mAdapter.addSectionHeaderItem(year+"   $"+df.format(yearList.get(year)));
                    itemList.add(year);
                    itemInfo.add("year");
                }
                if (i == 0)
                {
                    mAdapter.addSectionHeaderItem(months[Integer.parseInt(dts[1]) - 1]+"    $"+df.format(Float.parseFloat(onlyMonthList.get(dateCount))));
                    itemList.add(months[Integer.parseInt(dts[1]) - 1]);
                    itemInfo.add("month");
                    month = dts[1];
                    dateCount++;
                }
                else if (!month.equals(dts[1]))
                {
                    mAdapter.addSectionHeaderItem(months[Integer.parseInt(dts[1]) - 1]+"    $"+df.format(Float.parseFloat(onlyMonthList.get(dateCount))));
                    itemList.add(months[Integer.parseInt(dts[1]) - 1]);
                    itemInfo.add("month");
                    month = dts[1];
                    dateCount++;
                }
                for (int j = 0; j < expensesList.size(); j++) {
                    if (dateList.get(i).equals(expensesList.get(j).getDate())) {
                        String dtsLocal[] = expensesList.get(j).getDate().split("-");
                        if (dts[0].equals(dtsLocal[0]) && dts[1].equals(dtsLocal[1])) {
                            mAdapter.addItem(expensesList.get(j).getDate() + "\n" + expensesList.get(j).getDescription() + "\n" + "$" + expensesList.get(j).getAmount());
                            itemList.add(expensesList.get(j).getId() + "\n" + expensesList.get(j).getDate() + "\n" + expensesList.get(j).getDescription() + "\n" + "$" + expensesList.get(j).getAmount());
                            itemInfo.add("item");
                        }
                    }
                }
                setListAdapter(mAdapter);
                getListView().setOnItemClickListener(this);
            }
        }
        else
        {
            message.setTextColor(Color.rgb(255,0,0));
            message.setText("No expenses to display");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        manageExpense = (ManageExpense) getActivity();
        manageExpense.manageExpenseListner(i,itemList,itemInfo);
    }

    public void setMessage(String msg,String type)
    {
        this.msg = msg;
        typeMsg = type;
    }
}
interface ManageExpense
{
    public void manageExpenseListner(int position, ArrayList<String> itemList,ArrayList<String> itemInfo);
}
