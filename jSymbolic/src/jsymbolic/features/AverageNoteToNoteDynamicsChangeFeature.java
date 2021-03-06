/*
 * AverageNoteToNoteDynamicsChangeFeature.java
 * Version 1.2
 *
 * Last modified on April 11, 2010.
 * McGill University
 */

package jsymbolic.features;

import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic.processing.MIDIIntermediateRepresentations;


/**
 * A feature exractor that extracts the average change of loudness from one note
 * to the next note in the same channel (in MIDI velocity units).
 *
 * <p>No extracted feature values are stored in objects of this class.
 *
 * @author Cory McKay
 */
public class AverageNoteToNoteDynamicsChangeFeature
     extends MIDIFeatureExtractor
{
     /* CONSTRUCTOR ***********************************************************/
     
     
     /**
      * Basic constructor that sets the definition and dependencies (and their
      * offsets) of this feature.
      */
     public AverageNoteToNoteDynamicsChangeFeature()
     {
          String name = "Average Note To Note Dynamics Change";
          String description = "Average change of loudness from one note to the next note\n" +
               "in the same channel (in MIDI velocity units).";
          boolean is_sequential = true;
          int dimensions = 1;
          definition = new FeatureDefinition( name,
               description,
               is_sequential,
               dimensions );
          
          dependencies = null;
          
          offsets = null;
     }
     
     
     /* PUBLIC METHODS ********************************************************/
     
     
     /**
      * Extracts this feature from the given MIDI sequence given the other
      * feature values.
      *
      * <p>In the case of this feature, the other_feature_values parameters
      * are ignored.
      *
      * @param sequence			The MIDI sequence to extract the feature
      *                                 from.
      * @param sequence_info		Additional data about the MIDI sequence.
      * @param other_feature_values	The values of other features that are
      *					needed to calculate this value. The
      *					order and offsets of these features
      *					must be the same as those returned by
      *					this class's getDependencies and
      *					getDependencyOffsets methods
      *                                 respectively. The first indice indicates
      *                                 the feature/window and the second
      *                                 indicates the value.
      * @return				The extracted feature value(s).
      * @throws Exception		Throws an informative exception if the
      *					feature cannot be calculated.
      */
     public double[] extractFeature( Sequence sequence,
          MIDIIntermediateRepresentations sequence_info,
          double[][] other_feature_values )
          throws Exception
     {
          double value;
          if (sequence_info != null)
          {
               // Find the total number of intervals between notes on each channel
               int number_changes = 0;
               for (int i = 0; i < sequence_info.note_loudnesses.length; i++)
                    if (sequence_info.note_loudnesses[i].length > 1)
                         number_changes += sequence_info.note_loudnesses[i].length;
               
               // Find all of the loudness intervals
               double[] loudness_intervals = new double[number_changes];
               int count = 0;
               for (int i = 0; i < sequence_info.note_loudnesses.length; i++)
                    if (sequence_info.note_loudnesses[i].length > 1)
                         for (int j = 1; j < sequence_info.note_loudnesses[i].length; j++)
                         {
                    loudness_intervals[count] = (double)
                    Math.abs( sequence_info.note_loudnesses[i][j] -
                         sequence_info.note_loudnesses[i][j-1] );
                    count++;
                         }
               
               // Calculate the average
               value = mckay.utilities.staticlibraries.MathAndStatsMethods.getAverage(loudness_intervals);
          }
          else value = -1.0;
          
          double[] result = new double[1];
          result[0] = value;
          return result;
     }
}