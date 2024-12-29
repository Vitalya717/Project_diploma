package netology.web.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CartInfo {
        private String number;
        private String year;
        private String month;
        private String holder;
        private String cvc;
}
