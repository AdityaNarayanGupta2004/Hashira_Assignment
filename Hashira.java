import java.math.BigInteger;
import java.util.*;

public class Hashira {

    public static void main(String[] args) {
        System.out.println("=== Testcase 1 ===");
        double c1 = process(testcase1());
        System.out.println("\nConstant term (c₁) = " + c1);

        System.out.println("\n========================\n");

        System.out.println("=== Testcase 2 ===");
        double c2 = process(testcase2());
        System.out.println("\nConstant term (c₂) = " + c2);
    }

    // Process a single test case and return constant term
    private static double process(Map<String, Object> data) {
        int n = (int) data.get("n");
        int k = (int) data.get("k");

        List<double[]> points = new ArrayList<>();

        // ✅ Decode base/value pairs safely
        for (int i = 1; i <= n; i++) {
            Object[] entry = (Object[]) data.get(String.valueOf(i));
            if (entry == null) continue; // skip missing keys
            int base = (int) entry[0];
            String value = (String) entry[1];
            BigInteger bigY = new BigInteger(value, base);
            double y = bigY.doubleValue();
            points.add(new double[]{i, y});
        }

        // Sort points by x
        points.sort(Comparator.comparingDouble(p -> p[0]));

        // Build system of equations for interpolation
        double[][] A = new double[k][k];
        double[] Y = new double[k];
        for (int i = 0; i < k; i++) {
            double x = points.get(i)[0];
            double y = points.get(i)[1];
            for (int j = 0; j < k; j++) {
                A[i][j] = Math.pow(x, j);
            }
            Y[i] = y;
        }

        // Solve for coefficients
        double[] coeff = gaussianElimination(A, Y);

        // Print all coefficients
        System.out.println("Polynomial Coefficients (lowest degree to highest):");
        for (int i = 0; i < coeff.length; i++) {
            System.out.printf("a%d = %.6f%n", i, coeff[i]);
        }

        return coeff[0]; // return constant term c
    }

    // Gaussian elimination to solve Ax = b
    private static double[] gaussianElimination(double[][] A, double[] b) {
        int n = b.length;
        for (int i = 0; i < n; i++) {
            int max = i;
            for (int j = i + 1; j < n; j++)
                if (Math.abs(A[j][i]) > Math.abs(A[max][i]))
                    max = j;

            double[] temp = A[i];
            A[i] = A[max];
            A[max] = temp;

            double t = b[i];
            b[i] = b[max];
            b[max] = t;

            for (int j = i + 1; j < n; j++) {
                double factor = A[j][i] / A[i][i];
                b[j] -= factor * b[i];
                for (int k = i; k < n; k++)
                    A[j][k] -= factor * A[i][k];
            }
        }

        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < n; j++)
                sum += A[i][j] * x[j];
            x[i] = (b[i] - sum) / A[i][i];
        }
        return x;
    }

    // === Testcase 1 ===
    private static Map<String, Object> testcase1() {
        Map<String, Object> data = new HashMap<>();
        data.put("n", 4);
        data.put("k", 3);
        data.put("1", new Object[]{10, "4"});
        data.put("2", new Object[]{2, "111"});
        data.put("3", new Object[]{10, "12"});
        data.put("6", new Object[]{4, "213"});
        return data;
    }

    // === Testcase 2 ===
    private static Map<String, Object> testcase2() {
        Map<String, Object> data = new HashMap<>();
        data.put("n", 10);
        data.put("k", 7);
        data.put("1", new Object[]{6, "13444211440455345511"});
        data.put("2", new Object[]{15, "aed7015a346d635"});
        data.put("3", new Object[]{15, "6aeeb69631c227c"});
        data.put("4", new Object[]{16, "e1b5e05623d881f"});
        data.put("5", new Object[]{8, "316034514573652620673"});
        data.put("6", new Object[]{3, "2122212201122002221120200210011020220200"});
        data.put("7", new Object[]{3, "20120221122211000100210021102001201112121"});
        data.put("8", new Object[]{6, "20220554335330240002224253"});
        data.put("9", new Object[]{12, "45153788322a1255483"});
        data.put("10", new Object[]{7, "1101613130313526312514143"});
        return data;
    }
}

/*
import java.io.*;
import java.math.BigInteger;
import java.util.*;
import org.json.*;

public class HashiraPolynomial {
    public static void main(String[] args) {
        try {
            // Read JSON input
            String content = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get("input.json")));
            JSONObject obj = new JSONObject(content);

            JSONObject keys = obj.optJSONObject("keys");
            int n = keys.getInt("n");
            int k = keys.getInt("k");
            int degree = k - 1;

            // Collect root keys
            List<String> rootKeys = new ArrayList<>();
            for (String key : obj.keySet()) {
                if (!key.equals("keys")) rootKeys.add(key);
            }
            Collections.sort(rootKeys, Comparator.comparingInt(Integer::parseInt));

            BigInteger product = BigInteger.ONE;

            // Compute product of first degree roots
            for (int i = 0; i < degree && i < rootKeys.size(); i++) {
                JSONObject root = obj.getJSONObject(rootKeys.get(i));
                int base = root.getInt("base");
                String value = root.getString("value");
                BigInteger rootVal = new BigInteger(value, base);
                product = product.multiply(rootVal);
            }

            if (degree % 2 != 0) product = product.negate();

            System.out.println("Total roots (n): " + n);
            System.out.println("Polynomial degree (m): " + degree);
            System.out.println("Constant term (c): " + product);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
*/
