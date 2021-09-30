import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.SetVar;

import java.util.stream.IntStream;

public class SocialGolfer {

    int weeks, groups, size;
    SetVar[][] schedule;

    public SocialGolfer(int weeks, int groups, int size) {
        this.weeks = weeks;
        this.groups = groups;
        this.size = size;
    }

    Model get_choco_model() {
        Model sgp = new Model(
                "Social Golfer " + Integer.toString(weeks) + Integer.toString(groups) + Integer.toString(size));

        int[] weeks_set = IntStream.range(0, this.weeks).toArray();
        int[] groups_set = IntStream.range(0, this.groups).toArray();
        int[] golfers_set = IntStream.range(0, this.size * this.groups).toArray();

        schedule = sgp.setVarMatrix("schedule", weeks, groups, new int[] {}, golfers_set);
        IntVar[][] min_groups = sgp.intVarMatrix(weeks, groups, 0, this.size * this.groups);
        IntVar[] max_weeks = sgp.intVarArray(weeks, 0, this.size * this.groups);
        SetVar empty_set = sgp.setVar(new int[] {});
        IntVar size_const = sgp.intVar("size", size);

        for (int w : weeks_set) {
            for (int g1 : groups_set) {
                for (int g2 : groups_set) {
                    if (g1 < g2) {
                        sgp.intersection(new SetVar[] { schedule[w][g1], schedule[w][g2] }, empty_set).post();
                    }
                }
            }
        }

        for (int w : weeks_set) {
            for (int g : groups_set) {
                schedule[w][g].setCard(size_const);
                sgp.min(schedule[w][g], min_groups[w][g], true).post();
            }
            sgp.max(schedule[w][1], max_weeks[w], true).post();
        }

        for (int w1 : weeks_set) {
            for (int w2 : weeks_set) {
                if (w1 < w2) {
                    for (int g1 : groups_set) {
                        for (int g2 : groups_set) {
                            SetVar intersection = sgp.setVar(new int[] {}, golfers_set);
                            sgp.intersection(new SetVar[] { schedule[w1][g1], schedule[w2][g2] }, intersection).post();
                            intersection.setCard(sgp.intVar(0, 1));
                        }
                    }
                }
            }
        }

        for (int w : weeks_set) {
            for (int g : groups_set) {
                if (g != groups - 1) {
                    sgp.arithm(min_groups[w][g], "<", min_groups[w][g + 1]).post();
                }
            }
            if (w != weeks - 1) {
                sgp.arithm(max_weeks[w], "<", max_weeks[w + 1]).post();
            }
        }
        return sgp;
    }

    void solve_via_choco() {
        Model choco = get_choco_model();
        Solver solver = choco.getSolver();
        Solution solution = solver.findSolution();

        if (solution != null) {
            for (int w = 0; w < weeks; w++) {
                for (int g = 0; g < groups; g++) {
                    for (int i : solution.getSetVal(schedule[w][g])) {
                        System.out.print(i);
                        System.out.print(" ");
                    }
                    System.out.println();
                }
                System.out.println();
            }
        }
    }
}
