package route_finding;

import graph_impl.Destination;
import graph_impl.Graph;

import java.util.*;

public class Search_Agent {

    private Graph<Node> tGraph;
    private HashMap<State,Double> heuristic;

    public Search_Agent(Graph tGraph)
    {
        this.tGraph = tGraph;
    }

    public void Uninformed_Cost_Search(State initialState, State goalState)
    {
        Comparator<Node> c = Comparator.
                comparingDouble(Node::getPathCost);
        PriorityQueue<Node> frontier = new PriorityQueue<>(c);
        Stack<Node> reached = new Stack<>();

        Node parentNode = findNode(initialState);
        Node initialNode = parentNode;
        parentNode.setPathCost(0);

        parentNode.setParentNode(parentNode);
        reached.add(parentNode);

        if(parentNode.getState() == goalState)
        {
            printPath(reached,parentNode,parentNode,frontier);
            return;
        }
        else if(parentNode == null){
            System.out.println("Initial state is null!");
            return;
        }

        List<Destination<Node>> list;
        while (true)
        {
            list = tGraph.getAdjacencies(parentNode.getKey());
            if(list != null && !list.isEmpty())
            {
                for(Destination<Node> node : list)
                {
                    Node tempNode = new Node(
                            node.getT().getState(),
                            parentNode,
                            node.getWeight()+parentNode.getPathCost(),
                            node.getT());
                    if(parentNode.getParentNode().getState()
                            != tempNode.getState())
                    {
                        frontier.add(tempNode);
                        reached.add(tempNode);
                        tempNode.setParentNode(parentNode);
                    }
                    if(frontier.peek().getState() == goalState)
                    {
                        printPath(reached,frontier.peek(),initialNode,frontier);
                        return;
                    }
                }
                }

            parentNode = frontier.poll();
            if(parentNode == null)
            {
                System.out.println("No Path");
                return;
            }
        }
    }

    private void printPath(Stack<Node> reached, Node target, Node initialNode, Collection frontier)
    {
        System.out.println("************");
        System.out.println("Reached : "+reached.size());
        System.out.println("Frontier : "+frontier.size());
        StringBuilder b = new StringBuilder();
        while (true)
        {
            b.append(target.getState()+" <-- ");
            if(target == initialNode)
            {
                break;
            }
            target = target.getParentNode();
        }
        System.out.println(b);
    }

    private Node findNode(State state)
    {
        for (Object node : tGraph.keySet())
        {
            if(((Node)node).getState() == state)
            {
                return (Node) node;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        Env_Romania_Roads env_romania_roads = new Env_Romania_Roads();
        Search_Agent agent = new Search_Agent(env_romania_roads.getGraph());
        System.out.println("Uninformed Search");
        agent.Uninformed_Cost_Search(State.Arad,State.Bucharest);
    }
}
