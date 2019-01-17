package technikMobiler.bean;

public enum MessageCode {

    /** Code for discovering the current Coordinator of the network */
    CDIS("CDIS"),
    /** Code for Setting the Node's address */
    ADDR("ADDR"),
    /** Code for a simple message */
    MSSG("MSSG"),
    /** Code for self-polling to detect address collision */
    ALIV("ALIV"),
    /**Code for acknowledge permanent Address */
    AACK("AACK"),
    /**Code for Networkreset*/
    NRST("NRST");

    protected String code;

    MessageCode(String s) {
        this.code = s;
    };

    public String code() {
        return code;
    }

}
