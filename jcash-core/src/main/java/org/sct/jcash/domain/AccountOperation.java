package org.sct.jcash.domain;

import java.time.LocalDateTime;

public interface AccountOperation {
    LocalDateTime getOperationDate();
    double getAmount();
}
