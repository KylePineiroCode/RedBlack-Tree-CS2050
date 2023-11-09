//Kyle Pineiro
//Assignment 9
import java.io.FileWriter;
import java.io.IOException;
public class RedBlackTree
{
    //Initializing everything needed for the tree
    private Node root;
    private final Node TNULL;
    public final int RED = 1;
    private final int BLACK = 0;



    //Constructor for the RedBlack Tree
    public RedBlackTree()
    {
        TNULL = new Node("Key");
        TNULL.color = BLACK;
        TNULL.left = null;
        TNULL.right = null;
        root = TNULL;

    }
    private Node searchTree(Node node,String key)
    {
        //If the word doesn't exist create a node for it
        if (node == null || key.equals(node.word))
        {
            return node;
        }
        //Initiating the comparison method
        int comparison = key.compareTo(node.word);
        // If the comparison come alphabetically before root go to left subtree
        if (comparison < 0)
        {
            return searchTree(node.left, key);
        }
        // If comparison comes alphabetically after root got to the right subtree
        else
        {
            return searchTree(node.right, key);
        }
    }
    private void fixInsert(Node k)
    {
        Node u;
        //This is while the parent nodes color is red
        while(k.parent.color == RED)
        {
            if (k.parent == k.parent.parent.right)
            {
                u = k.parent.parent.left; //uncle

                if (u.color == RED)
                {
                    u.color = BLACK;
                    k.parent.color = BLACK;
                    k.parent.parent.color = RED;
                    k = k.parent.parent;
                }
                else
                {
                    if (k == k.parent.left)
                    {
                        k = k.parent;
                        rightRotate(k);
                    }
                    k.parent.color = BLACK;
                    k.parent.parent.color = RED;
                    leftRotate(k.parent.parent);
                }

            }
            else
            {
                u = k.parent.parent.right;
                if (u.color == RED)
                {
                    u.color = BLACK;
                    k.parent.color = BLACK;
                    k.parent.parent.color = RED;
                    k = k.parent.parent;
                }
                else
                {
                    if ( k == k.parent.right)
                    {
                        k = k.parent;
                        leftRotate(k);
                    }
                    k.parent.color = BLACK;
                    k.parent.parent.color = RED;
                    rightRotate(k.parent.parent);
                }
            }
            if (k == root)
            {
                break;
            }
        }
        root.color = BLACK;
    }
    public void leftRotate(Node x)
    {
        Node y = x.right;
        x.right = y.left;
        if(y.left != TNULL)
        {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if(x.parent == null)
        {
            this.root = y;
        }
        else if(x == x.parent.left)
        {
            x.parent.left = y;
        }
        else
        {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }
    public void rightRotate(Node x)
    {
        Node y = x.left;
        x.left = y.right;
        if (y.right != TNULL)
        {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if(x.parent == null)
        {
            this.root = y;
        }
        else if(x == x.parent.right)
        {
            x.parent.right = y;
        }
        else
        {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;

    }
    public void insert (String key)
    {
        key = key.toLowerCase();
        Node existingNode = searchTree(root, key);

        if (existingNode != null)
        {
            existingNode.count++;
        }
        else
        {
            Node node = new Node(key);
            node.parent = null;
            node.word = key;
            node.left = TNULL;
            node.right = TNULL;
            node.color = RED;

            Node y = null;
            Node x = this.root;

            while (x != TNULL)
            {
                y = x;
                int comparison2 = node.word.compareTo(x.word);
                if (comparison2 < 0)
                {
                    x = x.left;
                }
                else
                {
                    x = x.right;
                }
            }
            node.parent = y;
            if (y == null)
            {
                root = node;
            }
            else
            {
                int comparison3 = node.word.compareTo(y.word);
                if (comparison3 < 0)
                {
                    y.left = node;
                }
                else
                {
                    y.right = node;
                }
                if (node.parent == null)
                {
                    node.color = BLACK;
                    return;
                }
                if(node.parent.parent == null)
                {
                    return;
                }
                fixInsert(node);
            }

        }
    }
    public int countNodes(Node node)
    {
        if (node == null)
        {
            return 0;
        }
        return 1 + countNodes(node.left) + countNodes(node.right);
    }
    //Finding the max height for both left and right subtrees then adding 1 for the root
    //to find the maximum height of the tree
    public int getHeight(Node node)
    {
        if(node == null)
        {
            return -1;
        }
        int leftHeight = getHeight(node.left);
        int rightHeight = getHeight(node.right);
        return Math.max(leftHeight, rightHeight) + 1;
    }
    //This is the method that writes the information in the file
    public void analyzeDracula(String analysis) throws IOException
    {
        int count = searchTree(root, "the").count;
        int nodeCount = countNodes(root);
        int height = getHeight(root);
        long getMaxNodes = (long) Math.pow(2, height ) -1 ;

        try(FileWriter fw = new FileWriter("analysis.txt"))
        {
            fw.write("Kyle Pineiro \n");
            fw.write("The number occurrences of 'the' in the reading is: " + count + "\n");
            fw.write("The number of nodes is: " + nodeCount + "\n");
            fw.write("The height of the BST is: " + height + "\n");
            fw.write("The max nodes in the BST is: " + getMaxNodes + "\n");
        }
    }
    public void prettyPrintToFile() throws IOException
    {
        try(FileWriter fw = new FileWriter("rbtree.txt"))
        {
            prettyPrintToFile(root, fw, 0, 3);
        }
    }
    private void prettyPrintToFile(Node node, FileWriter fw, int level, int maxLevel) throws IOException
    {
        if (node != null && level <= maxLevel)
        {
            prettyPrintToFile(node.right, fw, level + 1, maxLevel);

            for (int i = 0; i < level; i++)
            {
                fw.write(" ");
            }
            fw.write(node.word + "(" + node.count + ")" + "\n");
            prettyPrintToFile(node.left, fw, level + 1, maxLevel);
        }
    }
    //Class to create the node for the tree
   class Node
    {
        String word;
        Node parent;
        Node left;
        Node right;
        int color;
        int count;

        public Node (String word)
        {
            this.word = word;
            this.count = 1;
            this.left = null;
            this.right = null;
        }
    }

}
