package blackrusemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.Settings.GameLanguage;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

import blackrusemod.powers.AmplifyDamagePower;
import blackrusemod.vfx.ServantDaggerEffect;

public class ThrowKnivesAction extends AbstractGameAction {
	private DamageInfo info;
	private boolean isRandom;
	private String debuff;
	public ThrowKnivesAction(AbstractPlayer p, AbstractCreature target, DamageInfo info, boolean isRandom, String debuff)
	{
		this.duration = com.megacrit.cardcrawl.core.Settings.ACTION_DUR_XFAST;
		this.actionType = AbstractGameAction.ActionType.DAMAGE;
		this.source = p;
		this.target = target;
		this.info = info;
		this.isRandom = isRandom;
		this.debuff = debuff;
	}

	public void update()
	{
		if (this.source.hasPower("KnivesPower")) {
			if (this.source.getPower("KnivesPower").amount <= 0) {
				if (Settings.language == GameLanguage.ZHS || Settings.language == GameLanguage.ZHT) {
					AbstractDungeon.actionManager.addToBottom(new TalkAction(true, "我的飞刀用光了！", 1.0F, 2.0F));
				}
				else {
				AbstractDungeon.actionManager.addToBottom(new TalkAction(true, "I have depleted my Knives!", 1.0F, 2.0F));
				}
			}
			else {
				if (this.isRandom) this.target = AbstractDungeon.getMonsters().getRandomMonster(true);
				if ((this.target != null) && !(this.target.isDying) && !(this.target.halfDead)) {
					AbstractDungeon.actionManager.addToTop(new DamageAction(this.target, this.info, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
					if ((this.target != null) && (this.target.hb != null)) {
						AbstractDungeon.actionManager.addToTop(new VFXAction(new ServantDaggerEffect(this.target.hb.cX, this.target.hb.cY)));
					}
					
					this.source.getPower("KnivesPower").reducePower(1);
					this.source.getPower("KnivesPower").updateDescription();
					
					if (this.source.hasPower("SurpressingFirePower")) this.source.addBlock(this.source.getPower("SurpressingFirePower").amount);
					if (this.debuff != null && this.debuff == "Weakened")  
						AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.target, this.source, new WeakPower(this.target, 1, false), 1));
					if (this.debuff != null && this.debuff == "Vulnerable")  
						AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.target, this.source, new VulnerablePower(this.target, 1, false), 1));
					if (this.debuff != null && this.debuff == "Amplify Damage")  {
						AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.target, this.source, new AmplifyDamagePower(this.target, 1), 1));
						if (AbstractDungeon.player.hasRelic("PaperSwan")) 
							if (AbstractDungeon.cardRandomRng.randomBoolean())
								AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.target, AbstractDungeon.player, new AmplifyDamagePower(this.target, 1), 1));
					}
				}
			}
		}
		else {
			if (Settings.language == GameLanguage.ZHS || Settings.language == GameLanguage.ZHT) {
				AbstractDungeon.actionManager.addToBottom(new TalkAction(true, "身上没有飞刀！", 1.0F, 2.0F));
			}
			else {
			AbstractDungeon.actionManager.addToBottom(new TalkAction(true, "I don't have any Knives!", 1.0F, 2.0F));
			}
		}

		this.isDone = true;
	}
}