package gov.ca.cwds.template;

/**
 * @author CWDS TPT-2 Team
 */
public final class Constants {

  public static final String Y = "Y";

  public static final String N = "N";

  public static final String SPACE = " ";

  public static final String SQL_TYPE = "sqlType";

  public static final String RETURNED_CLASS_NAME_PARAM = "returnedClassName";

  private Constants() {
  }

  public static class API {

    public static final String SYSTEM_INFORMATION_PATH = "system-information";

    public static final String TEMPLATE_INSTANCES_PATH = "template/instances";
    public static final String TEMPLATE_INSTANCE_TAG = "template-instances";

    private API() {
    }

    public static class PathParams {

      public static final String TEMPLATE_NAME_PARAMETER = "templateName";
      public static final String TEMPLATE_ID_PARAMETER = "templateId";


      private PathParams() {
      }
    }
  }

  public static class UnitOfWork {

    public static final String TEMPLATE_UNIT_OF_WORK = "templateUnitOfWork";

    private UnitOfWork() {
    }
  }

  public static class ExpectedExceptionMessages {

    private ExpectedExceptionMessages() {
    }

  }

  public static class Validation {

    private Validation() {
    }
  }
}
