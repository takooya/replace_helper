package org.example.i18n.temp;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.example.i18n.consts.CommonConstant.chineseLP;
import static org.example.i18n.consts.CommonConstant.startChineseLP;

public final class LineChecker {
    private static final LineChecker EMPTY = new LineChecker();

    private final String value;

    private LineChecker() {
        this.value = null;
    }

    public static LineChecker empty() {
        return EMPTY;
    }

    private LineChecker(String value) {
        this.value = Objects.requireNonNull(value).trim();
    }

    public static LineChecker of(String value) {
        return new LineChecker(value);
    }

    public String get() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    public boolean isPresent() {
        return value != null;
    }

    public void ifPresent(Consumer<? super String> consumer) {
        if (value != null) {
            consumer.accept(value);
        }
    }

    public String orElse(String other) {
        return value != null ? value : other;
    }

    public String orElseGet(Supplier<? extends String> other) {
        return value != null ? value : other.get();
    }

    public <X extends Throwable> String orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (value != null) {
            return value;
        } else {
            throw exceptionSupplier.get();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof LineChecker)) {
            return false;
        }
        LineChecker other = (LineChecker) obj;
        return Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return value != null
                ? String.format("LineChecker[%s]", value)
                : "LineChecker.empty";
    }

    private LineChecker commonChecker(boolean b) {
        if (!isPresent()) {
            return this;
        } else {
            return b ? this : empty();
        }
    }

    public LineChecker notStarComment() {
        return commonChecker(!value.startsWith("*"));
    }

    public LineChecker notLineComment() {
        return commonChecker(!value.startsWith("//"));
    }

    public LineChecker notAtApi() {
        return commonChecker(!value.startsWith("@Api"));
    }

    public LineChecker notExcel() {
        return commonChecker(!value.startsWith("@Excel"));
    }

    public LineChecker notLog() {
        return commonChecker(!value.startsWith("log."));
    }

    public LineChecker notPrint() {
        return commonChecker(!value.startsWith("System.out.print"));
    }

    public LineChecker notStartChina() {
        return commonChecker(!startChineseLP.matcher(value).find());
    }

    public LineChecker suffixFilter() {
        if (!isPresent()) {
            return this;
        } else {
            int i = value.indexOf("//");
            if (i != -1) {
                String filter = value.substring(0, i);
                if (!chineseLP.matcher(filter).find()) {
                    return empty();
                } else {
                    return LineChecker.of(filter);
                }
            } else {
                return this;
            }
        }
    }

    public LineChecker prefixFilter() {
        if (!isPresent()) {
            return this;
        } else {
            int i = value.indexOf("*/");
            if (i != -1) {
                String filter = value.substring(i);
                if (!chineseLP.matcher(filter).find()) {
                    return empty();
                } else {
                    return LineChecker.of(filter);
                }
            } else {
                return this;
            }
        }
    }

    public LineChecker starLineFilter() {
        int i = value.indexOf("/*");
        if (i != -1) {
            String filter = value.substring(0, i);
            if (!chineseLP.matcher(filter).find()) {
                return empty();
            } else {
                return LineChecker.of(filter);
            }
        } else {
            return this;
        }
    }

    public LineChecker keep(boolean comment) {
        return commonChecker(!comment);
    }

    public String[] split(String s) {
        if (!isPresent()) {
            return new String[0];
        } else {
            return value.split(s);
        }
    }
}
