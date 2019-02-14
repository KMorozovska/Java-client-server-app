package socket;

public class Protocol {

	/**
	 * Komenda nie przenoszaca zadnej informacji
	 */
	static final String NULLCOMMAND = "null";
	
	/**
	 * Zadanie zalogowania
	 */
    static final String LOGIN = "login";
    
    /**
     * Prosba o wyslanie hasla przez znanego klienta
     */
    static final String	ASKFORPASSWORD = "password";
    
    /**
     * Prosba o podanie hasla dla nowego klienta
     */
    static final String	NEWPASSWORD = "newpassword";    

    /**
     * Informacja o zalogowaniu klienta
     */
    static final String LOGGEDIN = "loggedin";
	
    /**
     * Informacja o checi wylogowania zgloszona przez klienta
     */
    static final String LOGOUT = "logout";

    /**
     * Informacja o wylogowaniu klienta
     */
    static final String LOGGEDOUT = "loggedout";
    
    /**
     * Zatrzymanie komunikacji klient-serwer
     */
    static final String STOP = "stop";
    
    /**
     * Informacja o checi zarchiwizowania pliku na serwerze
     */
    static final String ARCHIVIZEFILE = "archivizefile";
    
    /**
     * Informacja o zarchiwizowaniu pliku na serwerze
     */
    static final String FILEARCHIVIZED = "filearchivized";
    
    /**
     * Informacja o wysylaniu pliku
     */
    public static final String SENDINGFILE = "sendingfile";
    
    /**
     * Informacja o pobieraniu pliku
     */
    static final String DOWNLOADFILE = "downloadfile";
    
    /**
     * Informacja o powodzeniu pobierania pliku
     */
    static final String FILEDOWNLOADED = "filedownloaded";
    
    /**
     * Informacja o checi usuniecia pliku z serwera
     */
    static final String REMOVEFILE = "removefile";
    
    /**
     * Informacja o powodzeniu usuniecia pliku z serwera
     */
    static final String FILEREMOVED = "fileremoved";
    
    /**
     * Informacja o poprawnym pobraniu pliku
     */
    static final String RECEIVEDFILE = "receivedfile";
    
    /**
     * Rozkaz wysylany do serwera by ten wyslal aktualna liste zarchiwizowanych na nim plikow
     */
    static final String UPDATEFILES = "updatefiles";
    
    /**
     * Sprawdzenie czy dany plik znajduje sie juz na serwerze
     */
    static final String ISONSERVER = "isonserver";
    
    /**
     * Informacja, ze identyczny plik znajduje sie juz na serwerze
     */
    static final String SAMEFILEARCHIVIZED = "thesame";
	
}
