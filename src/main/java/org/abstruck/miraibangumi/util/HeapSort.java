package org.abstruck.miraibangumi.util;

public class HeapSort {
    public static final HeapSort INSTANCE = new HeapSort();

    public <T extends Sortable> void sort(T[] arr,SortMode mode){
        int heapLen = arr.length;
        for (int i = heapLen-1; i>=0;i--){
            heapify(arr, i, heapLen,mode);
        }

        for (int i = heapLen -1;i>0;i--){
            swap(arr, 0, heapLen-1);
            heapLen--;
            heapify(arr, 0, heapLen,mode);
        }
    }

    public <T extends Sortable> void heapify(T[] arr,int idx,int heapLen,SortMode mode){
        int left = idx * 2 + 1, right = idx * 2 +2, largest = idx;
    
        if(left < heapLen && arr[left].sortValue(mode) > arr[largest].sortValue(mode)){
            largest = right;
        }
        if(right < heapLen && arr[right].sortValue(mode) > arr[largest].sortValue(mode)){
            largest = right;
        }

        if(largest != idx){
            swap(arr, largest, idx);
            heapify(arr, largest, heapLen,mode);
        }
    }

    private <T> void swap(T[] arr,int idx1,int idx2){
        T tmp = arr[idx1];
        arr[idx1] = arr[idx2];
        arr[idx2] = tmp;
    }
}
