package com.epam.jwd.hrmanager.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class ConnectionServiceContext {

    private final static Logger LOGGER = LogManager.getLogger(ConnectionServiceContext.class);

    private Integer minimalConnectionNum;
    private Long collectorTimeInterval;
    private Double expansionLevel;
    private Double expansionRatio;
    private Double compressionRatio;

    public Optional<Integer> getMinimalConnectionNum() {
        return Optional.ofNullable(minimalConnectionNum);
    }

    public Optional<Long> getCollectorTimeInterval() {
        return Optional.ofNullable(collectorTimeInterval);
    }

    public Optional<Double> getExpansionLevel() {
        return Optional.ofNullable(expansionLevel);
    }

    public Optional<Double> getExpansionRatio() {
        return Optional.ofNullable(expansionRatio);
    }

    public Optional<Double> getCompressionRatio() {
        return Optional.ofNullable(compressionRatio);
    }

    public static Builder of() {
        return new ConnectionServiceContext().new Builder();
    }

    public class Builder {

        public Builder setMinimalConnectionNum(int minimalConnectionNum) {
            if (validatePercent(minimalConnectionNum)) {
                ConnectionServiceContext.this.minimalConnectionNum = minimalConnectionNum;
            } else {
                ConnectionServiceContext.this.minimalConnectionNum = null;
            }
            return this;
        }

        public Builder setCollectorTimeInterval(long millis) {
            ConnectionServiceContext.this.collectorTimeInterval = millis;
            return this;
        }

        public Builder setExpansionLevel(double percent) {
            if (validatePercent(percent)) {
                expansionLevel = percent;
            } else {
                expansionLevel = null;
            }
            return this;
        }

        public Builder setExpansionRatio(double percent) {
            if (validatePercent(percent)) {
                expansionRatio = percent;
            } else {
                expansionRatio = null;
            }
            return this;
        }

        public Builder setCompressionRatio(double percent) {
            if (validatePercent(percent)) {
                compressionRatio = percent;
            } else {
                expansionRatio = null;
            }
            return this;
        }

        public ConnectionServiceContext build() {
            return ConnectionServiceContext.this;
        }

        private boolean validatePercent(double number) {
            if (number > 0 && number < 100) {
                return true;
            } else {
                LOGGER.warn("Invalid data!");
                return false;
            }
        }
    }
}
