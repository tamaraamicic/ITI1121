/**
 * This class enables the construction of a decision tree
 * 
 * @author Mehrdad Sabetzadeh, University of Ottawa
 * @author Guy-Vincent Jourdan, University of Ottawa
 *
 */

public class DecisionTree {

	private static class Node<E> {
		E data;
		Node<E>[] children;

		Node(E data) {
			this.data = data;
		}
	}

	Node<VirtualDataSet> root;

	/**
	 * @param data is the training set (instance of ActualDataSet) over which a
	 *             decision tree is to be built
	 */
	public DecisionTree(ActualDataSet data) {
		root = new Node<VirtualDataSet>(data.toVirtual());
		build(root);
	}

	/**
	 * The recursive tree building function
	 * 
	 * @param node is the tree node for which a (sub)tree is to be built
	 */
	@SuppressWarnings("unchecked")
	private void build(Node<VirtualDataSet> node) {
		// WRITE YOUR CODE HERE!
		//System.out.println();
		//System.out.println("-----METHOD CALL-----");

		// EDGE CASES ??
		if (node == null || node.data == null){
			throw new NullPointerException("NullPointerException");
		}

		if (node.data.getNumberOfAttributes() < 1){
			throw new IllegalStateException("The node has less than one attribute");
		}

		if (node.data.getNumberOfDatapoints() < 1){
			throw new IllegalStateException("The node has less than one datapoint");
		}

		// BASE CASES 
		//System.out.println("node.data: " + node.data);
		//2.1
		if (node.data.getNumberOfAttributes() == 1 && node.data.getAttribute(0).equals
			(node.data.getSourceDataSet().attributes[node.data.getSourceDataSet().attributes.length-1])) {
			//System.out.println("base case 1");
			return;

		//2.2
		} else if(node.data.getUniqueAttributeValues(node.data.attributes.length-1).length == 1){
			//System.out.println("base case 2");
			return;

		//2.3
		} else if (noAttributeHasMoreThanOneUniqueValue(node.data)) {
			//System.out.println("base case 3");
			return;
		}
		
		// RECURSIVE CASE

		// 3.1
		GainInfoItem[] gains = InformationGainCalculator.calculateAndSortInformationGains(node.data);

		Attribute a_max = node.data.getAttribute(gains[0].getAttributeName()); 
		String splitString = gains[0].getSplitAt();

		// System.out.println("splitString: " + splitString);

		// System.out.println("a_max: " + a_max.getName());
		
		
		//3.2
		VirtualDataSet[] partitions;

		if (a_max.getType() == AttributeType.NOMINAL){
			partitions = node.data.partitionByNominallAttribute(node.data.getAttributeIndex(a_max.getName()));
		}else{
			//?????????????????
			int splitIndex = 0;
			for (int i = 0; i < node.data.getUniqueAttributeValues(a_max.getName()).length; i++){
				if(node.data.getUniqueAttributeValues(a_max.getName())[i].equals(splitString)) {
					splitIndex = i;
				}
			}
			
			partitions = node.data.partitionByNumericAttribute(node.data.getAttributeIndex(a_max.getName()), splitIndex);
			 
		}	

		//3.3
		node.children = (Node<VirtualDataSet>[]) new Node[partitions.length];

		//3.4 and 3.5
		for (int i = 0; i < partitions.length; i++) {
			node.children[i] = new Node(partitions[i]);
			build(node.children[i]);
		}

	}

	private boolean noAttributeHasMoreThanOneUniqueValue(VirtualDataSet data) {
		for (int i = 0; i < data.getNumberOfAttributes()-1; i++) {
			if (data.getUniqueAttributeValues(i).length > 1) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return toString(root, 0);
	}

	/**
	 * The recursive toString function
	 * 
	 * @param node        is the tree node for which an if-else representation is to
	 *                    be derived
	 * @param indentDepth is the number of indenting spaces to be added to the
	 *                    representation
	 * @return an if-else representation of node
	 */
	private String toString(Node<VirtualDataSet> node, int indentDepth) {
		// WRITE YOUR CODE HERE!

		StringBuffer buffer = new StringBuffer();

		//base cases
		
		if (node.data.getNumberOfAttributes() == 1 && node.data.getAttribute(0).equals
			(node.data.getSourceDataSet().attributes[node.data.getSourceDataSet().attributes.length-1])) {
			return (createIndent(indentDepth) + node.data.getAttribute(node.data.attributes.length-1).getName() + " = " + node.data.getUniqueAttributeValues((node.data.attributes.length-1))[0] + "\n");
		}
		
		if (node.data.getUniqueAttributeValues(node.data.attributes.length-1).length == 1){
			return (createIndent(indentDepth) + node.data.getAttribute(node.data.attributes.length-1).getName() + " = " + node.data.getUniqueAttributeValues((node.data.attributes.length-1))[0]+ "\n");
		}

		if (noAttributeHasMoreThanOneUniqueValue(node.data)) {
			return (createIndent(indentDepth) + node.data.getAttribute(node.data.attributes.length-1).getName() + " = " + node.data.getUniqueAttributeValues((node.data.attributes.length-1))[0]+ "\n");
		}

		//recursive case

		for (int i = 0; i < node.children.length; i++) {
			if (i == 0) {
				buffer.append(createIndent(indentDepth) + "if (" + node.children[i].data.getCondition() + ") {" + "\n");
			} else {
				buffer.append(createIndent(indentDepth) + "else if (" + node.children[i].data.getCondition() + ") {" + "\n");
			}
			buffer.append(toString(node.children[i], indentDepth + 1));
			buffer.append(createIndent(indentDepth) + "}" + "\n");
		}

		// Remove the following line once you have implemented the method
		return buffer.toString();
	}

	/**
	 * @param indentDepth is the depth of the indentation
	 * @return a string containing indentDepth spaces; the returned string (composed
	 *         of only spaces) will be used as a prefix by the recursive toString
	 *         method
	 */
	private static String createIndent(int indentDepth) {
		if (indentDepth <= 0) {
			return "";
		}
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < indentDepth; i++) {
			buffer.append("  ");
		}
		return buffer.toString();
	}

	public static void main(String[] args) throws Exception {
	
		StudentInfo.display();

		// if (args == null || args.length == 0) {
		// 	System.out.println("Expected a file name as argument!");
		// 	System.out.println("Usage: java DecisionTree <file name>");
		// 	return;
		// }

		// String strFilename = args[0];

		// ActualDataSet data = new ActualDataSet(new CSVReader(strFilename));

		// DecisionTree dtree = new DecisionTree(data);

		// System.out.println(dtree);

		ActualDataSet data1 = new ActualDataSet(new CSVReader("weather-nominal.csv"));
		DecisionTree dtree1 = new DecisionTree(data1);

		System.out.println("*** Decision tree for weather-nominal.csv ***");
		System.out.println();

		System.out.println(dtree1);

		System.out.println("*** Decision tree for weather-numeric.csv ***");
		System.out.println();

		ActualDataSet data2 = new ActualDataSet(new CSVReader("weather-numeric.csv"));
		DecisionTree dtree2 = new DecisionTree(data2);

		System.out.println(dtree2);

	}
}