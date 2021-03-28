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
		if (node.data.getNumberOfAttributes() == 1 && node.data.getAttribute(0).equals(node.data.getSourceDataSet().attributes[-1])) {

		}else if(node.data.getUniqueAttributeValues(0).length == 1){

		}
		


		// RECURSIVE CASE
		GainInfoItem[] gains = InformationGainCalculator.calculateAndSortInformationGains(node.data);

		Attribute a_max; 
		String maxName;
		double maxGainValue = gains[0].getGainValue();

		for (int i = 0; i < gains.length; i++){
			if (gains[i].getGainValue() > maxGainValue) {
				maxGainValue = gains[i].getGainValue();
				maxName = gains[i].getAttributeName();
			}
		}

		for(int i = 0; i < node.data.getNumberOfAttributes(); i++){
			if (node.data.attributes
		}

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
		
		// Remove the following line once you have implemented the method
		return null;
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

		if (args == null || args.length == 0) {
			System.out.println("Expected a file name as argument!");
			System.out.println("Usage: java DecisionTree <file name>");
			return;
		}

		String strFilename = args[0];

		ActualDataSet data = new ActualDataSet(new CSVReader(strFilename));

		DecisionTree dtree = new DecisionTree(data);

		System.out.println(dtree);
	}
}