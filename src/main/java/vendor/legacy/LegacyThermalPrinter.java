package vendor.legacy;

public final class LegacyThermalPrinter {
    public void init() {
        System.out.println("[LegacyPrinter] Initialized");
    }

    public void printLine(String line) {
        System.out.println("[LegacyPrinter] " + line);
    }

    public void cut() {
        System.out.println("[LegacyPrinter] --- CUT ---");
    }
}


