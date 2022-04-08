/*
 * PhysicsHair.h
 *
 *  Created on: 2010/11/29
 *      Author: nakajo
 *  [[ CONFIDENTIAL ]]
 */

#ifndef PHYSICSHAIR_H_
#define PHYSICSHAIR_H_


#include "../Live2D.h"
#include "../ALive2DModel.h"
#include <vector>
#include <math.h>


class IPhysicsSrc ;
class IPhysicsTarget ;

//=================================================
//=================================================
class PhysicsPoint {
public:
	float mass ;//����(kg)
	
	float x  , y ;//�ʒu(m)
	float vx ,vy ;//���x(m/s)
	float ax ,ay ;//�����x(m/s2)
	
	float fx ,fy ;//�O��(

	float last_x , last_y ;
	float last_vx , last_vy ;
	
	PhysicsPoint()
		: mass(1) , x(0) ,y(0) ,vx(0),vy(0) ,ax(0),ay(0) , fx(0) , fy(0)
		, last_x(0) , last_y(0) , last_vx(0) , last_vy(0)
	{
	}
	
	void setupLast(){
		this->last_x = x ;
		this->last_y = y ;
		this->last_vx = vx ;
		this->last_vy = vy ;
	}
	
} ;

//=================================================
//=================================================
/**
 * ���̖т��^���I�ɕ������Z����N���X
 * 
 * �Q�̓_���^���I�Ȕ��̖тƂ݂Ȃ��ď�������
 * 
 */
class PhysicsHair {
public:
	typedef enum {
		SRC_TO_X = 0 ,
		SRC_TO_Y ,
		SRC_TO_G_ANGLE 
	} Src ;

	typedef enum {
		TARGET_FROM_ANGLE = 0 ,//���̖у��f���̂��̊p�x
		TARGET_FROM_ANGLE_V , //���̖у��f���̂��̊p���x�i���h��̕ό`�����͑��x�����Ɉˑ�����j
	} Target ;

	
	PhysicsHair() ;
	PhysicsHair(float baseLengthM , float airRegistance, float mass ) ;
	virtual ~PhysicsHair();

	/**
     * ���̖т̒����i���[�g���j
     * ��C��R�i�f�t�H���g�l 0.5�j
     * ���̖т̎��ʁiKg�j
     */
	void setup(float baseLengthM , float airRegistance, float mass ) ;

	//�O������_�̈ʒu�������Ȃǂ��s�����ꍇ�ɌĂ�
	void setup() ;


	//���̖т̊�_�� X �ɕR�Â���l
	void addSrcParam( PhysicsHair::Src srcType , const char * paramID , float scale , float weight ) ;

	//���̖т̊�_�� X �ɕR�Â���l
	void addTargetParam( PhysicsHair::Target targetType , const char * paramID , float scale , float weight ) ;
	
	
	void update(live2d::ALive2DModel * model , long long time);

	PhysicsPoint & getPhysicsPoint1(){ return p1 ; }
	PhysicsPoint & getPhysicsPoint2(){ return p2 ; }

	float getGravityAngleDeg(){ return this->gravityAngleDeg ; }
	void setGravityAngleDeg(float angleDeg){ this->gravityAngleDeg = angleDeg ; }
	
	float getAngleP1toP2Deg(){ return angleP1toP2Deg ; }
	float getAngleP1toP2Deg_velocity(){ return angleP1toP2Deg_v ; }
	

private:
    void update_exe(live2d::ALive2DModel *& model, float dt);

	inline float calc_angleP1toP2(){
		return -180*(float)(L2D_ATAN2(p1.x - p2.x, -(p1.y - p2.y)))/M_PI ;
	}

    //
    PhysicsPoint p1 ;//���_�P�i���̍����j
	PhysicsPoint p2 ;//���_�Q�i���̐�[�j

    float baseLengthM;
    float gravityAngleDeg;
    float airResistance;

    float angleP1toP2Deg ;//�v�Z����
	float last_angleP1toP2Deg ;

	float angleP1toP2Deg_v ;//�e���x�̌v�Z����

    //--- �������
    long long startTime;
    long long lastTime;

    //�������Z�Ɋ֌W�Â��錳�̃p�����[�^�l
    std::vector<IPhysicsSrc*> srcList ;

    //�������Z���ʂ�Ή��Â���p�����[�^�l
    std::vector<IPhysicsTarget*> targetList ;

};



#endif /* PHYSICSHAIR_H_ */
