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

		//2.1
		if (node.data.getNumberOfAttributes() == 1 && node.data.getAttribute(0).equals(node.data.getSourceDataSet().attributes[-1])) {
			//????
		//2.2
		} else if(node.data.getUniqueAttributeValues(0).length == 1){
			//return node.data.getUniqueAttributeValues(0)[0];
		//2.3
		} else if (noAttributeHasMoreThanOneUniqueValue(node.data)) {
			//return something NO
		}
		
		// RECURSIVE CASE

		// 3.1
		GainInfoItem[] gains = InformationGainCalculator.calculateAndSortInformationGains(node.data);

		Attribute a_max = node.data.getAttribute(0); 
		double maxGainValue = gains[0].getGainValue();
		String splitString = gains[0].getSplitAt();

		for (int i = 1; i < gains.length; i++){
			if (gains[i].getGainValue() > maxGainValue) {
				maxGainValue = gains[i].getGainValue();
				a_max = node.data.getAttribute(gains[i].getAttributeName());
				splitString = gains[i].getSplitAt();
			}
		}

		//3.2
		
		VirtualDataSet[] partitions;

		if (a_max.getType() == AttributeType.NOMINAL){
			partitions = node.data.partitionByNominallAttribute(a_max.getAbsoluteIndex());
		}else{
			int split = Integer.parseInt(splitString);
			partitions = node.data.partitionByNumericAttribute(a_max.getAbsoluteIndex(), split);
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
		for (int i = 0; i < data.getNumberOfAttributes() - 1; i++) {
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
		String output = "if (" + node.data + ") is \'" + node.children[0] + "\') {" + createIndent(indentDepth) 
		+ "\n"+ toString(node.children[0], indentDepth + 1) + "\n"+ createIndent(indentDepth) + "class = " + 
		node.data.getUniqueAttributeValues(0)[0] + "}";
		
		// Remove the following line once you have implemented the method
		return output;
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
			buffer.append(' ');
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