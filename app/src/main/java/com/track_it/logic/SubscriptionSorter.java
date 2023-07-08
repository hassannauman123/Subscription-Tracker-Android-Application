package com.track_it.logic;

import com.track_it.domainobject.SubscriptionObj;
import com.track_it.logic.SubscriptionCompare.SubscriptionComparer;

import java.util.List;

public class SubscriptionSorter
{
    private SubscriptionComparer sorter = null; //How we will sort lists


    public SubscriptionSorter(SubscriptionComparer inputSortCriteria)
    {
        sorter  = inputSortCriteria;
    }

    public void setSortCriteria(SubscriptionComparer inputSortCriteria)
    {
        sorter  = inputSortCriteria;
    }


    public void sortSubscriptions(List<SubscriptionObj> listToSort)
    {
        if ( sorter != null)
        {
            mergeSortSubs(listToSort, 0, listToSort.size() - 1);
        }
    }



    //Merge sort two lists
    private  void mergeSortSubs( List<SubscriptionObj> listToSort, int indexLow, int indexHigh   )
    {

        if ( indexLow < indexHigh )
        {
            int newMid = indexLow  + ( indexHigh - indexLow)/2;

           mergeSortSubs(listToSort, indexLow, newMid);
           mergeSortSubs(listToSort, newMid+1, indexHigh);
           mergeSubList(listToSort,indexLow, newMid,indexHigh );
        }

    }

    //Merge two sorted lists in order
    private void mergeSubList( List<SubscriptionObj> listToSort,int indexLow, int midIndex ,int indexHigh)
    {

        int size1 = midIndex - indexLow +1;
        int size2 = indexHigh -midIndex;


        // Create temp arrays
        SubscriptionObj left[] = new SubscriptionObj[size1];
        SubscriptionObj right[] = new SubscriptionObj[size2];


        // Copy data to temp arrays
        for (int i = 0; i < size1; i++)
        {
            left[i] = listToSort.get(indexLow + i);
        }

        for (int j = 0; j < size2; j++)
        {
             right[j] = listToSort.get(midIndex  + 1+ j);

        }



        // Initial indices of first and second subArrays
        int firstIndex = 0, secondIndex = 0;

        // Initial index of merged subArray
        int k = indexLow;


        //Copy the two arrays into the list in order
        while (firstIndex < size1 && secondIndex < size2) {
           if ( sorter.compareSubscriptions( left[firstIndex], right[secondIndex] )  < 0 )
            {
                listToSort.set(k, left[firstIndex]);
                firstIndex++;
            }
            else {
                listToSort.set(k, right[secondIndex]);
                secondIndex++;
            }
            k++;
        }

        // Copy remaining elements of left[] if any
        while (firstIndex < size1) {
            listToSort.set(k,left[firstIndex]);
            firstIndex++;
            k++;
        }

        // Copy remaining elements of right[] if any
        while (secondIndex < size2) {
            listToSort.set(k, right[secondIndex]);
            secondIndex++;
            k++;
        }




    }







}
