package Server;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private List<Cartes> _cartes;

    public Main() {
        _cartes = new ArrayList<>();
    }

    public List<Cartes> getCartes() {
        return this._cartes;
    }

    public Cartes getCartesId(int i) {
        if (i < _cartes.size())
            return (_cartes.get(i));
        return (null);
    }

    public void addCartes(Cartes a) {
        _cartes.add(a);
    }

    public void deleteCartes(int id) {
        _cartes.remove(id);
    }
}
