package blackrusemod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import blackrusemod.BlackRuseMod;

public class KillerInstinctPower extends AbstractPower {
	public static final String POWER_ID = "KillerInstinctPower";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
	private int DEXTERITY = 0;

	public KillerInstinctPower(AbstractCreature owner, int amount) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		updateDescription();
		this.img = BlackRuseMod.getKillerInstinctPowerTexture();
	}
	
	public void stackPower(int stackAmount)
	{
		this.fontScale = 8.0F;
		this.amount += stackAmount;
	}
	
	public void atStartOfTurn() {
		this.DEXTERITY = 0;
		if (this.owner.hasPower("Weakened")) this.DEXTERITY += AbstractDungeon.player.getPower("Weakened").amount*this.amount;
		if (this.owner.hasPower("Vulnerable")) this.DEXTERITY += AbstractDungeon.player.getPower("Vulnerable").amount*this.amount;
		if (this.owner.hasPower("Frail")) this.DEXTERITY += AbstractDungeon.player.getPower("Frail").amount*this.amount;
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new DexterityPower(this.owner, this.DEXTERITY), this.DEXTERITY));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new LoseDexterityPower(this.owner, this.DEXTERITY), this.DEXTERITY));
	}

	public void updateDescription()
	{
		this.description = (DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1]);
	}
}