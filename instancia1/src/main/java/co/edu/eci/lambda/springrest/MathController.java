package co.edu.eci.lambda.springrest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MathController {

    @GetMapping("/fibonacci")
    public FibonacciResult getFibonacci(@RequestParam(value = "value") int n) {
        return new FibonacciResult(n, calculateFibonacci(n));
    }

    private List<Long> calculateFibonacci(int n) {
        List<Long> sequence = new ArrayList<>();
        if (n <= 0) return sequence;
        
        long a = 0, b = 1;
        sequence.add(a);
        if (n == 1) return sequence;
        
        sequence.add(b);
        for (int i = 2; i < n; i++) {
            long next = a + b;
            sequence.add(next);
            a = b;
            b = next;
        }
        return sequence;
    }
}

record FibonacciResult(int value, List<Long> sequence) {}
