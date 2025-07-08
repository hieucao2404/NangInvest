package test;

public class ClasspathTest {
  public static void main(String[] args) {
    System.out.println("=== ENHANCED CLASSPATH DEBUG ===");
    String classpath = System.getProperty("java.class.path");
    String[] paths = classpath.split(System.getProperty("path.separator"));

    System.out.println("Current runtime classpath:");
    boolean foundJaxbApi = false;
    boolean foundJaxbRuntime = false;
    boolean foundJaxbCore = false;
    boolean foundIstack = false;
    boolean foundActivationApi = false;
    boolean foundAngusActivation = false;

    for (String path : paths) {
      System.out.println("- " + path);

      if (path.contains("jakarta.xml.bind-api")) {
        System.out.println("  *** JAXB API FOUND ***");
        foundJaxbApi = true;
      }
      if (path.contains("jaxb-runtime")) {
        System.out.println("  *** JAXB RUNTIME FOUND ***");
        foundJaxbRuntime = true;
      }
      if (path.contains("jaxb-core")) {
        System.out.println("  *** JAXB CORE FOUND ***");
        foundJaxbCore = true;
      }
      if (path.contains("istack-commons-runtime")) {
        System.out.println("  *** ISTACK COMMONS FOUND ***");
        foundIstack = true;
      }
      if (path.contains("jakarta.activation-api")) {
        System.out.println("  *** JAKARTA ACTIVATION API FOUND ***");
        foundActivationApi = true;
      }
      if (path.contains("angus-activation")) {
        System.out.println("  *** ANGUS ACTIVATION FOUND ***");
        foundAngusActivation = true;
      }
    }

    System.out.println("\n=== DEPENDENCY CHECK ===");
    System.out.println("JAXB API: " + (foundJaxbApi ? "✅ FOUND" : "❌ MISSING"));
    System.out.println("JAXB Runtime: " + (foundJaxbRuntime ? "✅ FOUND" : "❌ MISSING"));
    System.out.println("JAXB Core: " + (foundJaxbCore ? "✅ FOUND" : "❌ MISSING"));
    System.out.println("iStack Commons: " + (foundIstack ? "✅ FOUND" : "❌ MISSING"));
    System.out.println("Jakarta Activation API: " + (foundActivationApi ? "✅ FOUND" : "❌ MISSING"));
    System.out.println("Angus Activation: " + (foundAngusActivation ? "✅ FOUND" : "❌ MISSING"));

    System.out.println("\n=== CLASS AVAILABILITY TEST ===");

    // Test JAXB classes
    try {
      Class.forName("jakarta.xml.bind.JAXBException");
      System.out.println("✅ jakarta.xml.bind.JAXBException is available");
    } catch (ClassNotFoundException e) {
      System.out.println("❌ jakarta.xml.bind.JAXBException NOT FOUND");
    }

    // Test Pool class
    try {
      Class.forName("com.sun.istack.Pool");
      System.out.println("✅ com.sun.istack.Pool is available");
    } catch (ClassNotFoundException e) {
      System.out.println("❌ com.sun.istack.Pool NOT FOUND");
    }

    // Test Jakarta Activation
    try {
      Class.forName("jakarta.activation.DataSource");
      System.out.println("✅ jakarta.activation.DataSource is available");
    } catch (ClassNotFoundException e) {
      System.out.println("❌ jakarta.activation.DataSource NOT FOUND");
    }

    // Test Hibernate
    try {
      Class.forName("org.hibernate.jpa.HibernatePersistenceProvider");
      System.out.println("✅ org.hibernate.jpa.HibernatePersistenceProvider is available");
    } catch (ClassNotFoundException e) {
      System.out.println("❌ org.hibernate.jpa.HibernatePersistenceProvider NOT FOUND");
    }

    System.out.println("\n=== SUMMARY ===");
    boolean allDepsFound = foundJaxbApi && foundJaxbRuntime && foundJaxbCore &&
        foundIstack && foundActivationApi && foundAngusActivation;

    if (allDepsFound) {
      System.out.println("🎉 ALL REQUIRED DEPENDENCIES FOUND! JPA should work now.");
    } else {
      System.out.println("⚠️  Some dependencies are missing. Check the list above.");
    }
  }
}
