package beans;

/**
 *
 * @author Dragan
 */
public class CategortyAndCards {

    private Category category = new Category();
    private int tickets = 0;

    public CategortyAndCards() {}
    
    public CategortyAndCards(Category category, int tickets) {
        this.category = category;
        this.tickets = tickets;
    }
    
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getTickets() {
        return tickets;
    }

    public void setTickets(int tickets) {
        this.tickets = tickets;
    }

    
    
    
}
