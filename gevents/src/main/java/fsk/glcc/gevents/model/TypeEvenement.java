package fsk.glcc.gevents.model;

public enum TypeEvenement {
	CONFERENCE("Conférence"),
	ATELIER("Atelier"),
	SOIREE("Soirée");
    
    private final String displayValue;
    
    private TypeEvenement(String displayValue) {
        this.displayValue = displayValue;
    }
    
    public String getDisplayValue() {
        return displayValue;
    }
}
