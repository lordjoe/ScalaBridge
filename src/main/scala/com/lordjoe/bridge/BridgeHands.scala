/*
 *  Copyright 2015 Steve Lewis
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *   com.lordjoe.bridge.Main
 */
package com.lordjoe.bridge

import java.awt.{Frame, GraphicsDevice, GraphicsEnvironment}
import javax.swing._

import com.lordjoe.bridge.sample._
import solitarius.ui._

object Main {

  def InterestingDeal: Deal = {
    BridgeDealer.makeHands
  }

  val frame = new JFrame("Bridge Hands")

  val DEFAULT_SIZE: Int = 900

  def setFrameSize(): Unit = {
    val gd: GraphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    val width: Int = gd.getDisplayMode().getWidth();
    val height: Int = gd.getDisplayMode().getHeight();

    frame.setSize(Math.min(DEFAULT_SIZE, width), Math.min(DEFAULT_SIZE, height))

    frame.setExtendedState( frame.getExtendedState() | Frame.MAXIMIZED_BOTH);
  }

  def main(args: Array[String]) {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)
    frame.setJMenuBar(menuBar)
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    setFrameSize()
    frame.setLocationRelativeTo(null)
    frame.setVisible(true)
    newGame(InterestingDeal)
  }

  def newGame(deal: Deal) {
    frame.getContentPane.removeAll()
    frame.getContentPane.add(new DealView(deal))
    frame.pack()
    setFrameSize()
  }

  def menuBar =
    MenuBarBuilder.buildMenuBar { menuBar =>
      menuBar.button("New Game") {
        game =>
          game.action("New Game") {
            newGame(InterestingDeal)
          }
      }
      menuBar.button("Strong NT") {
        game =>
          game.action("Strong NT") {
            newGame(SampleGenerator.findSatisfyingHand( StrongNT))
          }
      }
      menuBar.button("Too Strong NT") {
        game =>
          game.action("Too Strong NT") {
            newGame(SampleGenerator.findSatisfyingHand( TooStrongNT))
          }
      }
      menuBar.button("Weak NT") {
        game =>
          game.action("Weak NT") {
            newGame(SampleGenerator.findSatisfyingHand( WeakNT))
          }
      }
      menuBar.button("Slam?") {
          game =>
            game.action("Slam?") {
              newGame(SampleGenerator.findSatisfyingHand( MaybeSlam))
            }
        }
      menuBar.button("1 Club-> 2N") {
          game =>
            game.action("Slam?") {
              newGame(SampleGenerator.findSatisfyingHand( OpenOneClubThenTwoNo))
            }
        }
        menuBar.submenu("Bid") { quit =>
        quit.action("Interesting") {
          newGame(SampleGenerator.findSatisfyingHand(SampleGenerator.InterestingHand))
        }
          quit.action("2 Clubs") {
            newGame(SampleGenerator.findSatisfyingHand(OpenTwoClubs))
          }
          quit.action("2 Clubs -> 2NT") {
             newGame(SampleGenerator.findSatisfyingHand(OpenOneClubThenTwoNo))
           }
        quit.action("1 Diamond") {
          newGame(SampleGenerator.findSatisfyingHand(OpenOneDiamonds))
        }
        quit.action("2 Diamonds") {
          newGame(SampleGenerator.findSatisfyingHand(OpenTwoDiamonds))
        }
      }
      menuBar.submenu("Quit") { quit =>
        quit.action("Quit") {
          System.exit(0)
        }
      }
      menuBar.submenu("Help") { help =>
        help.action("About Bridge Dealer") {
          showAbout()
        }
      }
    }

  def showAbout() {
    val message =
      "Bridge Dealer 0.3\n" +
        "\n" +
        "Copyright 2015 Steven Lewis\n"

    JOptionPane.showMessageDialog(
      frame, message,
      "About Bridge Dealer", JOptionPane.INFORMATION_MESSAGE)
  }
}