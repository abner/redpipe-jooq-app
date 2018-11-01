
package io.abner.vertx;

import java.time.LocalDateTime;
import javax.enterprise.context.RequestScoped;


@RequestScoped
public class MeuBean {

    LocalDateTime date;
    public MeuBean() {
        date = LocalDateTime.now();
    }

    public String getData() {
        return String.format("DATE TIME: %s", this.date.toString());
    }
}