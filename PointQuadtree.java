import java.util.ArrayList;

public class PointQuadtree {

    enum Quad {
        NW,
        NE,
        SW,
        SE
    }

    public PointQuadtreeNode root;

    public PointQuadtree() {
        this.root = null;
    }
    
    private PointQuadtreeNode insertHelper(PointQuadtreeNode root, CellTower a){
        if (root==null){
            root = new PointQuadtreeNode(a);
            for (int i =0; i<4; i++){
                root.quadrants[i]=null;
            }
            return root;
        }else{
            if (a.equals(root.celltower)){
                return null;
            }else if (a.x<root.celltower.x && a.y>=root.celltower.y){
                root.quadrants[0] = insertHelper(root.quadrants[0], a);
            }else if (a.x>=root.celltower.x && a.y>root.celltower.y){
                root.quadrants[1] = insertHelper(root.quadrants[1], a);
            }else if (a.x<=root.celltower.x && a.y<root.celltower.y){
                root.quadrants[2] = insertHelper(root.quadrants[2], a);
            }else{
                root.quadrants[3] = insertHelper(root.quadrants[3], a);
            }
            return root;
        }
    }

    public boolean insert(CellTower a) {
        // TO be completed by students
        if (root==null){
            root = new PointQuadtreeNode(a);
            for (int i =0; i<4; i++){
                root.quadrants[i]=null;
            }
            return true;
        }
        if (cellTowerAt(a.x, a.y)){
            return false;
        }
        PointQuadtreeNode ans = insertHelper(root, a);
        if (ans==null){
            return false;
        }else{
            return true;
        }
    }

    private boolean cellTowerAtHelper(PointQuadtreeNode root, int x, int y) {
        if (root == null) {
            return false;
        } else if (root.celltower.x == x && root.celltower.y == y) {
            return true;
        } else if (x < root.celltower.x && y >= root.celltower.y) {
            return cellTowerAtHelper(root.quadrants[0], x, y);
        } else if (x >= root.celltower.x && y >= root.celltower.y) {
            return cellTowerAtHelper(root.quadrants[1], x, y);
        } else if (x < root.celltower.x && y < root.celltower.y) {
            return cellTowerAtHelper(root.quadrants[2], x, y);
        } else {
            return cellTowerAtHelper(root.quadrants[3], x, y);
        }
    }

    public boolean cellTowerAt(int x, int y) {
        // TO be completed by students
        if (root==null){
            return false;
        }
        return cellTowerAtHelper(root, x, y);
    }

    private void rangeSearch(PointQuadtreeNode root, int x, int y, int r, ArrayList<CellTower> towersInRange) {
        if (root == null) {
            return;
        }
        if (root.celltower.distance(x, y) <= r) {
            towersInRange.add(root.celltower);
        }
        if (root.quadrants != null) {
            int xMin = x - r;
            int xMax = x + r;
            int yMin = y - r;
            int yMax = y + r;
            for (PointQuadtreeNode quadrant : root.quadrants) {
                if (quadrant!=null){
                    if (quadrant.celltower.x <= xMax && quadrant.celltower.x >= xMin || quadrant.celltower.y <= yMax && quadrant.celltower.y >= yMin) {
                        rangeSearch(quadrant, x, y, r, towersInRange);
                    }
                }
            }
        }
    }

    public CellTower chooseCellTower(int x, int y, int r) {
        // TO be completed by students
        ArrayList<CellTower> towersInRange = new ArrayList<>();
        rangeSearch(root, x, y, r, towersInRange);
        if (towersInRange.isEmpty()) {
            return null;
        }
        CellTower cheapestTower = towersInRange.get(0);
        for (CellTower tower : towersInRange) {
            if (tower.cost < cheapestTower.cost) {
                cheapestTower = tower;
            }
        }
        return cheapestTower;
    }
    
    public static void main(String[] args) {
        PointQuadtree obj = new PointQuadtree();
        CellTower c1 = new CellTower(0,0,5);
        CellTower c2 = new CellTower(-2,0,4);
        CellTower c3 = new CellTower(2,3,10);
        CellTower c4 = new CellTower(-4,6,9);
        System.out.println(obj.insert(c1));
        System.out.println(obj.insert(c2));
        System.out.println(obj.insert(c3));
        System.out.println(obj.root.quadrants[1].celltower.x);
        System.out.println(obj.cellTowerAt(-2,0));
        System.out.println(obj.cellTowerAt(2,4));
        System.out.println(obj.chooseCellTower(0, 6, 5).cost);
        System.out.println(obj.insert(c4));
        System.out.println(obj.chooseCellTower(0, 6, 5).cost);
        // The current tree is shown in Figure 3.
        CellTower c5 = new CellTower(-3,7,5);
        CellTower c6 = new CellTower(-3,3,4);
        CellTower c7 = new CellTower(-6,7,2);
        CellTower c8 = new CellTower(-5,4,9);

        System.out.println(obj.insert(c5));
        System.out.println(obj.insert(c6));
        System.out.println(obj.insert(c7));
        System.out.println(obj.insert(c8));
        System.out.println(obj.insert(c3));

        System.out.println(obj.chooseCellTower(-2, 6, 2).cost);

    }

}
