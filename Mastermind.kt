import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JLabel
import javax.swing.ImageIcon
import javax.swing.JButton
import javax.swing.BoxLayout
import java.awt.BorderLayout
import java.awt.GridLayout
import java.awt.event.ActionListener
import kotlin.random.Random
import kotlin.random.nextInt

class Mastermind() : JFrame() {

	val panel1 = JPanel()
	val panel2 = JPanel()
	val panel3 = JPanel()
	val panel4 = JPanel()
	val panels = arrayListOf<JPanel>()
	val labelIcon = listOf("blue.png", "lightBlue.png", "green.png", "red.png", "orange.png", "yellow.png", "plus.png", "ex.png") // plus=6, ex=7
	val buttonIcon = listOf("blueButton.png", "lightBlueButton.png", "greenButton.png", "redButton.png", "orangeButton.png", "yellowButton.png", "restart.png", "backspace.png", "ok.png")
	var hiddenNumbers = arrayListOf<Int>()
	var userNumbers = arrayListOf<Int>()
	var count = 0
	var turn = 0
	var choice = true

	init {
		createGUI()
		cpuChoice()
	}
	
	fun createGUI() {
	
		setTitle("Mastermind")
		defaultCloseOperation = EXIT_ON_CLOSE
		setLayout(BorderLayout())
		
		panel1.setLayout(BoxLayout(panel1, BoxLayout.X_AXIS))
		panel2.setLayout(GridLayout(6, 0))
		panel3.setLayout(BoxLayout(panel3, BoxLayout.X_AXIS))// final result only
		panel4.setLayout(BoxLayout(panel4, BoxLayout.Y_AXIS))
		
		for (i in 0..8) {
			val button = JButton(ImageIcon(buttonIcon[i]))
			button.setContentAreaFilled(false)
			button.setBorderPainted(false)
			button.setFocusPainted(false)
			if (i < 6) {
				button.addActionListener({userChoice(i)})
				panel1.add(button)
				val panel = JPanel()
				panel.setLayout(BoxLayout(panel, BoxLayout.X_AXIS))
				panels.add(panel)
				panel2.add(panel)
			}else {
				button.addActionListener({selections(i)})
				panel4.add(button)
			}
		}
		
		add(panel1, BorderLayout.SOUTH)
		add(panel2, BorderLayout.WEST)
		add(panel3, BorderLayout.NORTH)
		add(panel4, BorderLayout.EAST)
		setLocationRelativeTo(null)
		pack()
		setVisible(true)
	}
	
	fun cpuChoice() {
	
		var cpu = generateSequence {Random.nextInt(0..5)}.distinct().take(4).toList()
		for (c in cpu) hiddenNumbers += c
		println(hiddenNumbers)
	}
	
	fun userChoice(num: Int) {
	
		if (choice) {
			panels[turn].add(JLabel(ImageIcon(labelIcon[num])))
			panels[turn].add(JLabel("   "))
			panel2.revalidate()
			panel2.repaint()
			userNumbers += num
			count++
			if (count == 4) choice = false
		}
	}
	
	fun selections(numb: Int) {
	
		if (numb == 6) restartGame()
		if (numb == 7) deleteColor()
		if (numb == 8) results()
	}
	
	fun deleteColor() {
	
		if (count > 0) {
			var components = panels[turn].getComponents()
			panels[turn].remove(components[components.size - 1])
			panels[turn].remove(components[components.size - 2])
			panel2.revalidate()
			panel2.repaint()
			userNumbers.remove(userNumbers[userNumbers.size - 1])
			choice = true
			count--
		}
	}
	
	fun results() {
	
		if (count == 4 && turn < 6) {
			if (userNumbers == hiddenNumbers) {
				(0 until 4).forEach {
					panels[turn].add(JLabel("   "))
					panels[turn].add(JLabel(ImageIcon(labelIcon[7])))
					panels[turn].add(JLabel("   "))
				}
				finalResult("You got it!")
				return
			}
			var unique = arrayListOf<Int>()
			for (i in 0..3) {
				if (userNumbers[i] == hiddenNumbers[i]) {
					panels[turn].add(JLabel("   "))
					panels[turn].add(JLabel(ImageIcon(labelIcon[7])))
					panels[turn].add(JLabel("   "))
					unique += userNumbers[i]
				}
			}
			for (i in 0..3) {
				if (userNumbers[i] in hiddenNumbers && !unique.contains(userNumbers[i])) {
					panels[turn].add(JLabel("   "))
					panels[turn].add(JLabel(ImageIcon(labelIcon[6])))
					panels[turn].add(JLabel("   "))
					unique += userNumbers[i]
				}
			}
			if (turn == 5) finalResult("Game over. The sequence is: ")
			panel2.revalidate()
			panel2.repaint()
			if (turn < 5) {
				userNumbers.clear()
				unique.clear()
				choice = true
				count = 0
				turn++
			}
		}
	}
	
	fun finalResult(msg: String) {
	
		panel3.add(JLabel("   "))
		panel3.add(JLabel(msg))
		panel3.add(JLabel("        "))
		for (hidden in hiddenNumbers) {
			panel3.add(JLabel(ImageIcon(labelIcon[hidden])))
			panel3.add(JLabel("   "))
		}
		count = 0
		panel3.revalidate()
		panel3.repaint()
	}
	
	fun restartGame() {
	
		for (t in 0..turn) panels[t].removeAll()
		panel3.removeAll()
		panel2.revalidate()
		panel2.repaint()
		panel3.revalidate()
		panel3.repaint()
		hiddenNumbers.clear()
		userNumbers.clear()
		choice = true
		count = 0
		turn = 0
		cpuChoice()
	}
	
	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
		
			Mastermind()
		}
	}
}
