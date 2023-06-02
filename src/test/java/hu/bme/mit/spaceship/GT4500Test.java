package hu.bme.mit.spaceship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GT4500Test {

  private TorpedoStore primaryTorpedoStore;

  private TorpedoStore secondaryTorpedoStore;

  private GT4500 ship;

  @BeforeEach
  public void init(){
    this.primaryTorpedoStore = mock(TorpedoStore.class);
    this.secondaryTorpedoStore = mock(TorpedoStore.class);
    this.ship = new GT4500(primaryTorpedoStore, secondaryTorpedoStore);
  }

  @Test
  void fireTorpedo_Single_Success(){
    when(primaryTorpedoStore.isEmpty()).thenReturn(false);
    when(secondaryTorpedoStore.isEmpty()).thenReturn(false);
    // Arrange
    when(primaryTorpedoStore.fire(1)).thenReturn(true);
    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertTrue(result);
    verify(primaryTorpedoStore, times(1)).fire(1);
    verify(secondaryTorpedoStore, never()).fire(1);
  }

  @Test
  void fireTorpedo_FiresOnceOnlySecondaryHasTorpedo() {
    // Arrange
    when(primaryTorpedoStore.isEmpty()).thenReturn(true);
    when(secondaryTorpedoStore.isEmpty()).thenReturn(false);
    when(secondaryTorpedoStore.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertTrue(result);
    verify(primaryTorpedoStore, never()).fire(1);
    verify(secondaryTorpedoStore, times(1)).fire(1);
  }

  @Test
  void fireTorpedo_FiresAllBothHaveTorpedoes() {
    // Arrange
    when(primaryTorpedoStore.isEmpty()).thenReturn(false);
    when(secondaryTorpedoStore.isEmpty()).thenReturn(false);
    when(primaryTorpedoStore.fire(1)).thenReturn(true);
    when(secondaryTorpedoStore.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertTrue(result);
    verify(primaryTorpedoStore, times(1)).fire(1);
    verify(secondaryTorpedoStore, times(1)).fire(1);
  }
  @Test
  void fireTorpedo_FiresAllPrimaryHasTorpedoes() {
    // Arrange
    when(primaryTorpedoStore.isEmpty()).thenReturn(false);
    when(secondaryTorpedoStore.isEmpty()).thenReturn(false);
    when(primaryTorpedoStore.fire(1)).thenReturn(true);
    when(secondaryTorpedoStore.fire(1)).thenReturn(false);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertFalse(result);
    verify(primaryTorpedoStore, times(1)).fire(1);
    verify(secondaryTorpedoStore, times(1)).fire(1);
  }
  @Test
  void fireTorpedo_FiresAllSecondaryHasTorpedoes() {
    // Arrange
    when(primaryTorpedoStore.isEmpty()).thenReturn(false);
    when(secondaryTorpedoStore.isEmpty()).thenReturn(false);
    when(secondaryTorpedoStore.fire(1)).thenReturn(true);
    when(primaryTorpedoStore.fire(1)).thenReturn(false);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertFalse(result);
    verify(secondaryTorpedoStore, never()).fire(1);
    verify(primaryTorpedoStore, times(1)).fire(1);
  }

  @Test
  void fireTorpedo_FiresThriceSecondaryHasMoreTorpedoes() {
    // Arrange
    when(primaryTorpedoStore.isEmpty()).thenReturn(false, false, true);
    when(secondaryTorpedoStore.isEmpty()).thenReturn(false);
    when(primaryTorpedoStore.fire(1)).thenReturn(true);
    when(secondaryTorpedoStore.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL) & ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertTrue(result);
    verify(primaryTorpedoStore, times(2)).fire(1);
    verify(secondaryTorpedoStore, times(1)).fire(1);
  }

  @Test
  void fireTorpedo_FireTwicePrimaryOnlyHasOne() {
    // Arrange
    when(primaryTorpedoStore.isEmpty()).thenReturn(false, true);
    when(secondaryTorpedoStore.isEmpty()).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE) & ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertFalse(result);
    verify(primaryTorpedoStore, times(1)).fire(1);
    verify(secondaryTorpedoStore, never()).fire(1);
  }

  @Test
  void fireTorpedo_DoesNotFireWhenBothStoresAreEmpty() {
    // Arrange
    when(primaryTorpedoStore.isEmpty()).thenReturn(true);
    when(secondaryTorpedoStore.isEmpty()).thenReturn(true);
    when(secondaryTorpedoStore.fire(1)).thenReturn(false);
    when(secondaryTorpedoStore.fire(1)).thenReturn(false);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertFalse(result);
    verify(primaryTorpedoStore, never()).fire(1);
    verify(secondaryTorpedoStore, never()).fire(1);
  }

  @Test
  void fireTorpedo_DoesNotFireAllWhenBothStoresAreEmpty() {
    // Arrange
    when(primaryTorpedoStore.isEmpty()).thenReturn(true);
    when(secondaryTorpedoStore.isEmpty()).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertFalse(result);
    verify(primaryTorpedoStore, times(1)).fire(1);
    verify(secondaryTorpedoStore, never()).fire(1);
  }

  @Test
  void fireTorpedo_FireTwiceBothHaveTorpedoes() {
    // Arrange
    when(primaryTorpedoStore.isEmpty()).thenReturn(false);
    when(secondaryTorpedoStore.isEmpty()).thenReturn(false);
    when(primaryTorpedoStore.fire(1)).thenReturn(true);
    when(secondaryTorpedoStore.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE) & ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertTrue(result);
    verify(primaryTorpedoStore, times(1)).fire(1);
    verify(secondaryTorpedoStore, times(1)).fire(1);
  }

  @Test
  void fireTorpedo_FireTwiceOnlyPrimaryHasTorpedoes() {
    // Arrange
    when(primaryTorpedoStore.isEmpty()).thenReturn(false);
    when(secondaryTorpedoStore.isEmpty()).thenReturn(true);
    when(primaryTorpedoStore.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE) & ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertTrue(result);
    verify(primaryTorpedoStore, times(2)).fire(1);
    verify(secondaryTorpedoStore, never()).fire(1);
  }

  @Test
  void fireLaser_Failure(){
    // Act
    boolean result = ship.fireLaser(FiringMode.SINGLE);

    // Assert
    assertFalse(result);
  }

}
