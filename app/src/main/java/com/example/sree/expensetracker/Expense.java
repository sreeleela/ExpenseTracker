package com.example.sree.expensetracker;

import java.util.Date;

/**
 * Created by Sree on 5/8/2018.
 */

public class Expense
{
    private  int id;
    private String useremail;
    private String description;
    private float amount;
    private String date;

    public void setId(int id)
    {
        this.id = id;
    }
    public void setUseremail(String useremail)
    {
        this.useremail = useremail;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }
    public void setAmount(float amount)
    {
        this.amount = amount;
    }
    public void setDate(String date)
    {
        this.date = date;
    }

    public int getId()
    {
        return id;
    }
    public String getUseremail()
    {
        return useremail;
    }
    public String getDescription()
    {
        return description;
    }
    public float getAmount()
    {
        return amount;
    }
    public String getDate()
    {
        return date;
    }
}
