import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Digraph {
    private String graphname;
    private ArrayList<Node> nodes = new ArrayList<>();

    public Digraph(String graphname) {
        this.graphname = graphname;
    }

    public Digraph addNode(String nodeID) {
        if (exists(nodeID)) {
            System.out.println("Error: Node " + nodeID + " already exists.");
            System.exit(0);
        }
        Node n = new Node(nodeID);
        n.graph = this;
        nodes.add(n);
        return this;
    }

    public boolean exists(String nodeID) {
        for (Node n : nodes) {
            if (n.nodeID.equals(nodeID)) {
                return true;
            }
        }
        return false;
    }

    public Node getNode(String nodeID) {
        for (Node n : nodes) {
            if (n.nodeID.equals(nodeID)) {
                return n;
            }
        }
        return null;
    }

    public Node addNode(String nodeID, String nodeName) {
        if (exists(nodeID)) {
            System.out.println("Error: Node " + nodeID + " already exists.");
            System.exit(0);
        }
        Node n = new Node(nodeID, nodeName);
        n.graph = this;
        nodes.add(n);
        return n;
    }

    public Node link(String parentNodeID, String childNodeID) {
        Node parent = getNode(parentNodeID);
        if (parent == null) {
            System.out.print("JavaGraph: Node " + parentNodeID + " does not exist.");
            System.exit(0);
        }
        Node child = getNode(childNodeID);
        if (child == null) {
            System.out.print("JavaGraph: Node " + childNodeID + " does not exist.");
            System.exit(0);
        }
        parent.addChild(child);
        return child;
    }

    public void link(String parentNodeID, String childNodeID, String linkLabel) {
        link(parentNodeID, childNodeID).linkLabel = linkLabel;
    }

    public void generate(String filename) {
        try {
            PrintWriter writer = new PrintWriter(filename, "UTF-8");
            writer.println("digraph " + graphname + " {");
            for (Node n : nodes) {
                if (n.hasName())
                    writer.println("ID" + n.nodeID + " [label=\"" + n.nodeName + "\"];");
                else
                    writer.println("ID" + n.nodeID + " [label=\"" + n.nodeID + "\"];");
            }
            for (Node n : nodes) {
                if (n.children.size() > 0) {
                    for (Node c : n.children) {
                        if (c.hasLabel())
                            writer.println("ID" + n.nodeID + " -> ID" + c.nodeID + " [label=\"" + c.linkLabel + "\"];");
                        else
                            writer.println("ID" + n.nodeID + " -> ID" + c.nodeID + ";");
                    }
                }
            }
            writer.println("}");
            writer.close();
            System.out.println("Tree generated");
        } catch (FileNotFoundException e) {
            System.out.println("JavaGraph: " + filename + " could not be written to.");
        } catch (UnsupportedEncodingException e) {
            System.out.print("JavaGraph: " + e.getMessage());
        }
    }

    class Node {
        private String nodeID;
        private String nodeName;
        private String linkLabel;
        private ArrayList<Node> children = new ArrayList<>();
        private Digraph graph;

        public Node(String nodeID) {
            this.nodeID = nodeID;
        }

        public void setLabel(String label) {
            this.linkLabel = label;
        }

        public Node(String nodeID, String nodeName) {
            this.nodeID = nodeID;
            this.nodeName = nodeName;
        }

        public Node setName(String nodeName) {
            this.nodeName = nodeName;
            return this;
        }

        public Node addChild(Node newChild) {
            if (newChild == null) {
                return null;
            }
            this.children.add(newChild);
            return newChild;
        }

        public Node addChild(Node newChild, String linkLabel) {
            if (newChild == null) {
                return null;
            }
            newChild.linkLabel = linkLabel;
            this.children.add(newChild);
            return newChild;
        }

        public boolean hasName() {
            return (nodeName != null);
        }

        public boolean hasLabel() {
            return (linkLabel != null);
        }
    }
}