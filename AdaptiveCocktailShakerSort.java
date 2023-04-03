public class AdaptiveCocktailShakerSort. {
    public static void main(String[] args) {
        int[] arr = {5, 1, 9, 3, 7, 6, 8, 2, 4};
        adaptiveCocktailShakerSort(arr);
        for (int num : arr) {
            System.out.print(num + " ");
        }
    }

    public static void adaptiveCocktailShakerSort(int[] arr) {
        int left = 0;
        int right = arr.length - 1;
        int lastSwappedLeft = 0;
        int lastSwappedRight = 0;

        while (left < right) {
            // Move from left to right
            for (int i = left; i < right; i++) {
                if (arr[i] > arr[i + 1]) {
                    swap(arr, i, i + 1);
                    lastSwappedRight = i;
                }
            }
            right = lastSwappedRight;

            // Move from right to left
            for (int i = right; i > left; i--) {
                if (arr[i] < arr[i - 1]) {
                    swap(arr, i, i - 1);
                    lastSwappedLeft = i;
                }
            }
            left = lastSwappedLeft;
        }
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
