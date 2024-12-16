import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class GetStartLicenseStr {

    public static void main(String[] args) {
        String createLicenseStr = "";
        String productUUID = getProductUUID();
        createLicenseStr += productUUID;
        System.out.println("productUUID=" + getProductUUID());
        List<String> list = getMacAddresses();
        for (String macAddress : list) {
            System.out.println("macAddress=" + macAddress);
            createLicenseStr += macAddress;
        }
        System.out.println("createLicenseStr=" + createLicenseStr);

    }


    public static String getProductUUID() {
        String uuid = null;
        try {
            if (isWin()) {
                Process process = Runtime.getRuntime().exec(
                    "powershell Get-WmiObject -Class Win32_ComputerSystemProduct | Select-Object -ExpandProperty UUID");
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
                StringBuilder output = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line.trim().toLowerCase());
                }
                uuid = output.toString();
            } else {
                String uuidFilePath = "/sys/class/dmi/id/product_uuid";
                uuid = new String(Files.readAllBytes(Paths.get(uuidFilePath))).trim().toLowerCase();
            }
            uuid = Pattern.compile("[^a-f0-9]").matcher(uuid).replaceAll("");
            return uuid;
        } catch (Exception e) {
            e.printStackTrace();
            return uuid;
        }


    }

    public static List<String> getMacAddresses() {
        List<String> macAddresses = new ArrayList<>();
        try {
            if (isWin()) {
                Process process = Runtime.getRuntime().exec(
                    "wmic path Win32_NetworkAdapter where \"(PhysicalAdapter=True AND MACAddress IS NOT NULL)\" get MACAddress");
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty() && !line.equalsIgnoreCase("MACAddress")) {
                        macAddresses.add(line.toLowerCase());
                    }
                }
                return macAddresses;
            }

            File netDirectory = new File("/sys/class/net");
            if (!netDirectory.exists() || !netDirectory.isDirectory()) {
                System.err.println(
                    "Directory /sys/class/net does not exist or is not a directory.");
                return macAddresses;
            }
            File[] networkInterfaces = netDirectory.listFiles();
            if (networkInterfaces == null) {
                System.err.println("Failed to list files in /sys/class/net.");
                return macAddresses;
            }
            for (File interfaceDir : networkInterfaces) {
                File deviceDir = new File(interfaceDir, "device");
                if (deviceDir.exists() && deviceDir.isDirectory()) {
                    File addressFile = new File(interfaceDir, "address");
                    if (addressFile.exists() && !addressFile.isDirectory()) {
                        String macAddress = new String(
                            Files.readAllBytes(addressFile.toPath())).trim().toLowerCase();
                        macAddresses.add(macAddress);
                    }
                }
            }
            return macAddresses;
        } catch (Exception e) {
            e.printStackTrace();
            return macAddresses;
        }


    }

    public static boolean isWin() {
        String osName = System.getProperty("os.name");
        // 判断是否为Windows系统
        return osName.toLowerCase().contains("windows");
    }
}
